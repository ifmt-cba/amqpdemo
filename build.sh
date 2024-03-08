#!/bin/bash
rm -fr ./target
mvn clean package
mv ./target/*.jar ./target/app.jar

docker build -t br.edu.ifmt/amqpdemo --no-cache .