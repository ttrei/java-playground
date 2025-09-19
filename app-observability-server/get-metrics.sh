#!/usr/bin/env sh

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

cd "$SCRIPT_DIR" || exit 1

curl localhost:7654/user/123 > /dev/null &
curl localhost:7654/user/555 > /dev/null &
wait

curl localhost:7654/actuator/prometheus
