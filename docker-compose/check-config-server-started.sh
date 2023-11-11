#!/bin/bash

apt-get update -y

yes | apt-get install curl

result=$(curl -s -o /dev/null -I -w "%{http_code}" http://config-server:8888/actuator/health)

echo "result status code: " $result

while [[ ! $result == "200" ]]; do
  >&2 echo "Config server is not up yet!"
  sleep 2
  result=$(curl -s -o /dev/null -I -w "%{http_code}" http://config-server:8888/actuator/health)
done

./cnb/lifecycle/launcher
