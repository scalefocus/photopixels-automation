#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <compose-file>"
    exit 1
fi

available_service_count=$(docker compose config --services | wc -l)

docker compose -f "$1" up -d

sleep 2

# Check if all services had started
service_count=$(docker compose -f "$1" ps --services | wc -l)
if [ "$service_count" -ne "$available_service_count" ]; then
    echo "Not all services started"
    exit 1
fi

echo "All services started successfully"

i=10         # number of retries
sleep_time=1 # initial sleep time in seconds
max_sleep=30 # max sleep time in seconds

while ((i >= 0)); do
    wget_result=$(wget -NS --no-check-certificate http://localhost:8888/health -O /dev/null 2>&1 |
        grep "HTTP/" |
        awk '{print $2}' |
        tail -n 1)

    if [ "$wget_result" = "200" ]; then
        echo "Services started successfully."
        exit 0
    fi

    ((i--))

    echo "Services not ready. Waiting for $sleep_time seconds..."
    sleep "$sleep_time"

    # Exponential backoff with cap
    sleep_time=$((sleep_time * 2))
    if ((sleep_time > max_sleep)); then
        sleep_time=$max_sleep
    fi
done

while ((i >= 0)); do
    wget_result=$(wget -NS --no-check-certificate http://localhost:4444/status -O /dev/null 2>&1 |
        grep "HTTP/" |
        awk '{print $2}' |
        tail -n 1)

    if [ "$wget_result" = "200" ]; then
        echo "Selenium hub service started successfully."
        exit 0
    fi

    ((i--))

    echo "Selenium hub service not ready. Waiting for $sleep_time seconds..."
    sleep "$sleep_time"

    # Exponential backoff with cap
    sleep_time=$((sleep_time * 2))
    if ((sleep_time > max_sleep)); then
        sleep_time=$max_sleep
    fi
done

while ((i >= 0)); do
    wget_result=$(wget -NS --no-check-certificate http://localhost:4723/wd/hub/status -O /dev/null 2>&1 |
        grep "HTTP/" |
        awk '{print $2}' |
        tail -n 1)

    if [ "$wget_result" = "200" ]; then
        echo "Appium service started successfully."
        exit 0
    fi

    ((i--))

    echo "Appium service not ready. Waiting for $sleep_time seconds..."
    sleep "$sleep_time"

    # Exponential backoff with cap
    sleep_time=$((sleep_time * 2))
    if ((sleep_time > max_sleep)); then
        sleep_time=$max_sleep
    fi
done

docker compose -f "$1" logs
echo "Services did not become healthy in time."
exit 1
