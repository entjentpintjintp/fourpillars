#!/bin/bash

# 1. Load Environment Variables
if [ -f ./env.sh ]; then
    source ./env.sh
    echo "✅ Environment variables loaded."
else
    echo "❌ env.sh not found!"
    exit 1
fi

# 2. Start Infrastructure (MariaDB & Redis)
if command -v docker-compose &> /dev/null; then
    echo "🐳 Starting Docker services (MariaDB & Redis)..."
    docker-compose up -d
    
    # Wait for DB to be ready
    echo "⏳ Waiting for services to initialize..."
    sleep 10
else
    echo "⚠️ docker-compose not found. Assuming services are running locally."
fi

# 3. Check for JAR file
JAR_FILE=FourPillars-0.0.1-SNAPSHOT.jar
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found: $JAR_FILE"
    echo "   Please upload the JAR file to this directory."
    exit 1
fi

# 4. Run Application
echo "🚀 Starting FourPillars Application..."
java -jar $JAR_FILE
