#!/usr/bin/env bash

set -euo pipefail

main() {
  local build_image=$1
  local build_identifier=$2

  local container_id
  container_id=$(docker create "$build_image:$build_identifier")

  mkdir -p "builds/$build_identifier"

  set +e
  docker cp "$container_id:/home/worker/work/build/reports" "builds/$build_identifier" &&
  mv "builds/$build_identifier/reports" "builds/$build_identifier/build-reports"
  set -e
}

main "$1" "$2"
