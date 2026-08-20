#!/usr/bin/env bash

set -euo pipefail

if [[ $# -ne 1 ]]; then
  echo "Usage: $0 {auth|course|registration|gateway}" >&2
  exit 1
fi

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$ROOT_DIR/.env"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Missing $ENV_FILE. Create it from .env.example first." >&2
  exit 1
fi

case "$1" in
  auth) SERVICE_DIR="auth-service" ;;
  course) SERVICE_DIR="course-service" ;;
  registration) SERVICE_DIR="registration-service" ;;
  gateway) SERVICE_DIR="api-gateway" ;;
  *)
    echo "Unknown service: $1. Use auth, course, registration, or gateway." >&2
    exit 1
    ;;
esac

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

cd "$ROOT_DIR/services/$SERVICE_DIR"
exec mvn spring-boot:run
