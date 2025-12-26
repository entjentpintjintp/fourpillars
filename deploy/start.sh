#!/bin/bash

# 1. Load Environment Variables
if [ -f ./env.sh ]; then
    source ./env.sh
    echo "✅ Environment variables loaded."
else
    echo "❌ env.sh not found!"
    exit 1
fi

# 1-a. Stop Existing Application
echo "🛑 Stopping existing application..."
# Kill any java process running a jar with 'FourPillars' in the name
pkill -f 'java .*FourPillars' || echo "No running application found."
# Wait a moment for process to release port
sleep 5

# 2. Start Infrastructure (MariaDB & Redis)
if command -v docker-compose &> /dev/null; then
    echo "🐳 Starting Docker services (MariaDB & Redis)..."
    docker-compose up -d
    
    # Wait for DB to be ready
    echo "⏳ Waiting for services to initialize..."
    sleep 3
else
    echo "⚠️ docker-compose not found. Assuming services are running locally."
fi

# 3. Build Project (if source code exists)
PROJECT_ROOT=".."
# Dynamic JAR detection
JAR_PATH=$(find "$PROJECT_ROOT/build/libs" -name "FourPillars-*.jar" 2>/dev/null | head -n 1)

if [ -f "$PROJECT_ROOT/gradlew" ]; then
    echo "🔨 Building project with Gradle..."
    chmod +x "$PROJECT_ROOT/gradlew"
    (cd "$PROJECT_ROOT" && ./gradlew clean build -x test)
    
    if [ $? -ne 0 ]; then
        echo "❌ Build failed!"
        exit 1
    fi
    echo "✅ Build successful."
    
    # Re-find JAR after build as version might have changed
    JAR_PATH=$(find "$PROJECT_ROOT/build/libs" -name "FourPillars-*.jar" 2>/dev/null | head -n 1)
else
    echo "⚠️ Gradle wrapper not found. Assuming JAR exists."
    if [ -z "$JAR_PATH" ] && [ -f "FourPillars-*.jar" ]; then
        JAR_PATH=$(ls FourPillars-*.jar | head -n 1)
    fi
fi

# 4. Check for JAR file
if [ -z "$JAR_PATH" ] || [ ! -f "$JAR_PATH" ]; then
    echo "❌ JAR file not found. Checked build/libs and current dir."
    exit 1
fi

echo "🚀 Starting application: $(basename "$JAR_PATH")"

# 5. Run Application
nohup java -Duser.timezone=Asia/Seoul -jar "$JAR_PATH" > server.log 2>&1 &
echo "✅ Application started in background. Logs are being written to server.log"
echo "👉 Run 'tail -f server.log' to monitor."
