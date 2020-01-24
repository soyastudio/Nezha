#!/bin/bash
nohup java -jar pipeline-server.jar --spring.config.location=../conf/application.yaml > ../log/server.log 2>&1 &
echo $! > .pid