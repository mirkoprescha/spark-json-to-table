#!/usr/bin/env bash

#docker build -t mirkoprescha/spark-zeppelin .
docker build --file spark-docker/Dockerfile -t mirkoprescha/spark-zeppelin .


docker run -it -p 8080:8080   mirkoprescha/spark-zeppelin


#docker push mirkoprescha/spark-zeppelin