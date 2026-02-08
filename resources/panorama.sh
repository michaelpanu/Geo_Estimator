
PORT=8080

# start server if not already running
lsof -iTCP:$PORT -sTCP:LISTEN >/dev/null 2>&1 || (
  cd "$(dirname "$0")" && python3 -m http.server $PORT >/dev/null 2>&1 &
  sleep 0.2
)

URL="http://localhost:$PORT/panorama.html?lat=$1&lng=$2"
open -a "Google Chrome" "$URL" &
echo $!