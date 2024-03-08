#!/bin/bash
rm -fr ./target
mvn clean package
mv ./target/*.jar ./target/app.jar

docker build -t br.edu.ifmt/amqpdemo --no-cache .
docker rmi $(docker images -f "dangling=true" -q)