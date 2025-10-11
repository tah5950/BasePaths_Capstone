set -e # Exits upon failure

# CONFIGURATION
BACKEND_URL="http://localhost:8080"
FRONTEND_URL="http://localhost:5173"
DB_CONTAINER="basepaths-database"
BALLPARK_DATA="./database/processed_data/ballparks_2026.json"
SCHEDULE_DATA="./database/processed_data/schedule_2026.json"

echo "🚀 Starting Basepaths deployment..."

# 1. Check for docker

if ! command -v docker &> /dev/null; then
    echo "❌ Docker installation not found."
    exit 1
fi

if ! command -v docker compose &> /dev/null; then
    echo "❌ Docker Compose installation not found."
    exit 1
fi

# 2. Create Load Key
# LOAD_KEY=$(openssl rand -hex 16)
# export DATA_LOAD_KEY="$LOAD_KEY"
LOAD_KEY="0279fbdc-41e8-4080-90dc-2a28bd3340ba"
echo "🔑 Generated Load Key"

# 3. Remove old containers and rebuild
echo "🧹 Removing old containers"
docker compose down --remove-orphans

echo "🏗 Building containers..."
docker compose up -d --build

# 4. Wait for backend to run
echo "⏳ Waiting for backend to start..."
until curl -s "$BACKEND_URL/api/user/ping" | grep -q "pong"; do
    sleep 5
    echo "..."
done 
echo "✅ Backend Running"

# 5. Check that frontend is running
if curl -fs "$FRONTEND_URL" > /dev/null; then
    echo "✅ Frontend Running"
else
    echo "❌ Frontend Failed to start"
    exit 1
fi

# 6. Check that database is running
if docker exec "$DB_CONTAINER" pg_isready -U postgres > /dev/null; then
    echo "✅ Database Healthy"
else
    echo "❌ Database Failed to Start"
    exit 1
fi

# 7. Load seed data
echo "⚾ Loading ballpark data..."
curl -s -X POST "$BACKEND_URL/api/ballpark/load" \
    -H "X-LOAD-KEY: $LOAD_KEY" \
    -H "Content-Type: application/json" \
    -d @"$BALLPARK_DATA"

echo "📅 Loading ballpark data..."
curl -s -X POST "$BACKEND_URL/api/game/load" \
    -H "X-LOAD-KEY: $LOAD_KEY" \
    -H "Content-Type: application/json" \
    -d @"$SCHEDULE_DATA"

echo "✅ Data Load Finished"
echo "🎉 Deployment Successful!"