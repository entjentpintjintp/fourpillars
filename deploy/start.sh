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

# 3. Build Project (if source code exists)
PROJECT_ROOT=".."
JAR_PATH="$PROJECT_ROOT/build/libs/FourPillars-0.0.1-SNAPSHOT.jar"

if [ -f "$PROJECT_ROOT/gradlew" ]; then
    echo "🔨 Building project with Gradle..."
    chmod +x "$PROJECT_ROOT/gradlew"
    (cd "$PROJECT_ROOT" && ./gradlew clean build -x test)
    
    if [ $? -ne 0 ]; then
        echo "❌ Build failed!"
        exit 1
    fi
    echo "✅ Build successful."
else
    echo "⚠️ Gradle wrapper not found. Assuming JAR exists."
    # If no gradle, try to find jar in current dir (legacy support)
    if [ -f "FourPillars-0.0.1-SNAPSHOT.jar" ]; then
        JAR_PATH="FourPillars-0.0.1-SNAPSHOT.jar"
    fi
fi

# 4. Check for JAR file
if [ ! -f "$JAR_PATH" ]; then
    echo "❌ JAR file not found at: $JAR_PATH"
    exit 1
fi

# 5. Run Application
nohup java -Duser.timezone=Asia/Seoul -jar "$JAR_PATH" > server.log 2>&1 &
echo "✅ Application started in background. Logs are being written to server.log"
echo "👉 Run 'tail -f server.log' to monitor."
