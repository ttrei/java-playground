#!/usr/bin/env sh

uv run --with uvicorn --with fastapi uvicorn server:app --reload
