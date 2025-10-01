#!/usr/bin/env sh

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

cd "$SCRIPT_DIR" || exit 1

# parallel
if [ "$1" = "p" ]; then
    curl localhost:7654/user/123 > /dev/null &
    curl localhost:7654/user/123/full > /dev/null &
    curl -X POST localhost:7654/user/123/ping > /dev/null &
    curl -X POST localhost:7654/user/123/frob > /dev/null &
    curl localhost:7654/user/555/full > /dev/null &
    curl -X POST localhost:7654/user/555/ping > /dev/null &
    curl -X POST localhost:7654/user/555/frob > /dev/null &
    curl localhost:7654/user/555 > /dev/null &
    wait
fi

# sequential
if [ "$1" = "s" ]; then
    curl localhost:7654/user/123 > /dev/null
    curl localhost:7654/user/555 > /dev/null
    curl localhost:7654/user/123/full > /dev/null
    curl localhost:7654/user/555/full > /dev/null
    curl -X POST localhost:7654/user/123/ping > /dev/null
    curl -X POST localhost:7654/user/555/ping > /dev/null
    curl -X POST localhost:7654/user/123/frob > /dev/null
    curl -X POST localhost:7654/user/555/frob > /dev/null
fi

curl localhost:7654/actuator/prometheus
