#!/bin/bash

# Configuration
BACKUP_DIR="./backups"
CONTAINER_NAME="fourpillars-db"
DB_NAME="fourpillars"
DB_USER="root"
# Note: Password should be handled securely. 
# Here we assume the container has the env var set or we use the one from docker-compose.
# Using 'docker exec' allows us to run without hardcoding password in this script 
# if we leverage the internal env vars, but mysqldump usually requires explicit password argument.
# For simplicity and standard usage in this project context:
DB_ROOT_PASSWORD="0126" # Matches docker-compose.yml

# Date format for filename
DATE=$(date +%Y%m%d_%H%M)
FILENAME="backup_${DATE}.sql.gz"

# Create backup directory if not exists
mkdir -p "$BACKUP_DIR"

# Perform Backup
echo "Starting backup for $DB_NAME..."
docker exec "$CONTAINER_NAME" /usr/bin/mysqldump -u "$DB_USER" -p"$DB_ROOT_PASSWORD" "$DB_NAME" | gzip > "$BACKUP_DIR/$FILENAME"

# Check if backup succeeded
if [ $? -eq 0 ]; then
  echo "Backup successful: $BACKUP_DIR/$FILENAME"
  # Delete old backups (older than 7 days)
  find "$BACKUP_DIR" -type f -name "backup_*.sql.gz" -mtime +7 -exec rm {} \;
  echo "Old backups cleaned up."
else
  echo "Backup failed!"
  # Delete the empty/corrupted file if it exists
  rm -f "$BACKUP_DIR/$FILENAME"
  exit 1
fi
