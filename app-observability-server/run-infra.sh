#!/usr/bin/env sh

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

cd "$SCRIPT_DIR" || exit 1

docker compose up
