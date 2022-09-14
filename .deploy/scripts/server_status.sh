#!/bin/bash

sleep 20
i=0

while [[ $i -lt 30 ]]; do
  echo "Testing health, attempt $i"
  curl -s --noproxy '*' --insecure https://localhost:8443/actuator/health --fail >/dev/null
  SUCCESS=$?
  ((i++))
  if [[ $SUCCESS -eq 0 ]]; then
    echo "Health is up!"
    break
  fi
  sleep 2
done

exit $SUCCESS
