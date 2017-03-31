#!/usr/bin/env bash

docker build -t mirkoprescha/spark-zeppelin .
docker build --file spark-docker/Dockerfile -t mirkoprescha/spark-zeppelin .


#docker run -it -p 8088:8088 -p 8042:8042 -p 4040:4040 -h sandbox mirkoprescha/spark-zeppelin bash

docker run -it -p 8080:8080   mirkoprescha/spark-zeppelin