#FROM ubuntu:14.04
FROM openjdk:8-jre
MAINTAINER mirkoprescha

RUN apt-get -y update
RUN apt-get -y install curl

#RUN apt-get -y install python-software-properties
RUN apt-get -y install software-properties-common

# Install Java
RUN apt-get update && apt-get install -y default-jdk


# Zeppelin

RUN mkdir /usr/zeppelin
RUN curl -s http://mirrors.ae-online.de/apache/zeppelin/zeppelin-0.7.0/zeppelin-0.7.0-bin-all.tgz | tar -xz -C /usr/zeppelin

RUN echo '{ "allow_root": true }' > /root/.bowerrc

ENV ZEPPELIN_PORT 8080
ENV ZEPPELIN_HOME /usr/zeppelin/zeppelin-0.7.0-bin-all
ENV ZEPPELIN_CONF_DIR $ZEPPELIN_HOME/conf
ENV ZEPPELIN_NOTEBOOK_DIR $ZEPPELIN_HOME/notebook
ENV ZEPPELIN_COMMIT v0.7.0

RUN mkdir -p $ZEPPELIN_HOME \
  && mkdir -p $ZEPPELIN_HOME/logs \
  && mkdir -p $ZEPPELIN_HOME/run


#Spark 2.1 for Hadoop2.6
ARG SPARK_ARCHIVE=http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.6.tgz
ENV SPARK_HOME /usr/local/spark-2.1.0-bin-hadoop2.6

ENV PATH $PATH:${SPARK_HOME}/bin
RUN curl -s ${SPARK_ARCHIVE} | tar -xz -C /usr/local/

WORKDIR $SPARK_HOME

# Copy App
COPY spark-docker/bin/spark-json-to-table_2.11-1.0.jar /usr/local/bin/spark-json-to-table_2.11-1.0.jar

ADD  zeppelin_notebooks/dataset-analysis.json $ZEPPELIN_NOTEBOOK_DIR/2CE95BHSP/note.json

WORKDIR /home
ENTRYPOINT  $ZEPPELIN_HOME/bin/zeppelin-daemon.sh start  && bash

