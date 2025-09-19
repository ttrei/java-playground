#!/usr/bin/env sh

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

cd "$SCRIPT_DIR" || exit 1

curl localhost:7654/user/123
curl localhost:7654/user/555

curl localhost:7654/actuator/prometheus
