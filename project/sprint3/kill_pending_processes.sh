#!/bin/bash

# Extract unique PIDs from the lsof output
pids=$(lsof -i :8121,8122,8123 -sTCP:LISTEN,ESTABLISHED | grep java | awk '{print $2}' | sort -u)

if [ -z "$pids" ]; then
    echo "No Java processes found using ports 8121, 8122, or 8123"
    exit 0
fi

echo "Found the following Java processes using ports 8121, 8122, or 8123:"
for pid in $pids; do
    echo "PID: $pid - $(ps -p $pid -o comm=)"
done

echo ""
echo "Killing processes..."

for pid in $pids; do
    echo "Killing process $pid"
    kill $pid
    
    # Wait a moment to see if it terminates gracefully
    sleep 1
    
    # Check if process is still running
    if ps -p $pid > /dev/null; then
        echo "Process $pid didn't terminate gracefully, using force kill"
        kill -9 $pid
    else
        echo "Process $pid terminated successfully"
    fi
done

echo "All processes have been terminated"

