#!/bin/bash

# Configuration
CONTAINER_NAME="fourpillars-db"
DB_NAME="fourpillars"
DB_USER="root"
DB_ROOT_PASSWORD="0126" # Matches docker-compose.yml

if [ -z "$1" ]; then
  echo "Usage: ./restore.sh <backup_file.sql.gz>"
  exit 1
fi

BACKUP_FILE=$1

if [ ! -f "$BACKUP_FILE" ]; then
  echo "Error: File $BACKUP_FILE not found!"
  exit 1
fi

echo "WARNING: This will OVERWRITE the current database '$DB_NAME'."
read -p "Are you sure you want to continue? (y/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Restore cancelled."
    exit 1
fi

echo "Restoring from $BACKUP_FILE..."

# Unzip and pipe to mysql
gunzip < "$BACKUP_FILE" | docker exec -i "$CONTAINER_NAME" /usr/bin/mysql -u "$DB_USER" -p"$DB_ROOT_PASSWORD" "$DB_NAME"

if [ $? -eq 0 ]; then
  echo "Restore completed successfully."
else
  echo "Restore failed!"
  exit 1
fi
