#https://grzegorzgajda.gitbooks.io/spark-examples/content/basics/docker.html

FROM ubuntu:14.04
MAINTAINER mirkoprescha

RUN apt-get -y update
RUN apt-get -y install curl

#RUN apt-get -y install python-software-properties
RUN apt-get -y install software-properties-common

# JAVA
#ARG JAVA_ARCHIVE=http://download.oracle.com/otn-pub/java/jdk/8u102-b14/server-jre-8u102-linux-x64.tar.gz
#ENV JAVA_HOME /usr/local/jdk1.8.0_102
#ENV PATH $PATH:$JAVA_HOME/bin
#RUN curl -s --insecure \
#  --header "Cookie: oraclelicense=accept-securebackup-cookie;" ${JAVA_ARCHIVE} \
#  | tar -xz -C /usr/local/ && ln -s $JAVA_HOME /usr/local/java


# Install Java.
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle


# Zeppelin
#ENV ZEPPELIN_PORT 8080
#ENV ZEPPELIN_CONF_DIR $ZEPPELIN_HOME/conf
#ENV ZEPPELIN_NOTEBOOK_DIR $ZEPPELIN_HOME/notebook
#ENV ZEPPELIN_COMMIT v0.7.0
#RUN echo '{ "allow_root": true }' > /root/.bowerrc
#RUN mkdir /usr/zeppelin
#
#RUN curl -s http://mirrors.ae-online.de/apache/zeppelin/zeppelin-0.7.0/zeppelin-0.7.0-bin-all.tgz | tar -xz -C /usr/zeppelin
#
#ENV ZEPPELIN_HOME /usr/zeppelin/zeppelin-0.7.0-bin-all
#RUN mkdir -p $ZEPPELIN_HOME
#
#CMD ["$ZEPPELIN_HOME/bin/zeppelin-daemon.sh start"]


ENV ZEPPELIN_PORT 8080
ENV ZEPPELIN_HOME /usr/zeppelin
ENV ZEPPELIN_CONF_DIR $ZEPPELIN_HOME/conf
ENV ZEPPELIN_NOTEBOOK_DIR $ZEPPELIN_HOME/notebook
ENV ZEPPELIN_COMMIT v0.7.0
RUN echo '{ "allow_root": true }' > /root/.bowerrc
RUN set -ex \
 && buildDeps=' \
    git \
    bzip2 \
    npm \
 ' \
 && apt-get update && apt-get install -y --no-install-recommends $buildDeps \
 && curl -sL http://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz \
   | gunzip \
   | tar x -C /tmp/ \
 && git clone https://github.com/apache/zeppelin.git /usr/src/zeppelin \
 && cd /usr/src/zeppelin \
 && git checkout -q $ZEPPELIN_COMMIT \
 && dev/change_scala_version.sh "2.11" \
 && MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=1024m" /tmp/apache-maven-3.3.9/bin/mvn --batch-mode package -DskipTests -Pscala-2.11 -Pbuild-distr \
  -pl 'zeppelin-interpreter,zeppelin-zengine,zeppelin-display,spark-dependencies,spark,markdown,angular,shell,hbase,postgresql,jdbc,python,elasticsearch,zeppelin-web,zeppelin-server,zeppelin-distribution' \
 && tar xvf /usr/src/zeppelin/zeppelin-distribution/target/zeppelin*.tar.gz -C /usr/ \
 && mv /usr/zeppelin* $ZEPPELIN_HOME \
 && mkdir -p $ZEPPELIN_HOME/logs \
 && mkdir -p $ZEPPELIN_HOME/run \
 && apt-get purge -y --auto-remove $buildDeps \
 && rm -rf /var/lib/apt/lists/* \
 && rm -rf /usr/src/zeppelin \
 && rm -rf /root/.m2 \
 && rm -rf /root/.npm \
&& rm -rf /tmp/*

#Spark 2.1 for Hadoop2.6

ARG SPARK_ARCHIVE=http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.6.tgz
ENV SPARK_HOME /usr/local/spark-2.1.0-bin-hadoop2.6

ENV PATH $PATH:${SPARK_HOME}/bin
RUN curl -s ${SPARK_ARCHIVE} | tar -xz -C /usr/local/

WORKDIR $SPARK_HOME

# Copy App
COPY spark-docker/bin/spark-json-to-table_2.11-1.0.jar /usr/local/bin/spark-json-to-table_2.11-1.0.jar

ADD  zeppelin_notebooks/dataset-analysis.json $ZEPPELIN_NOTEBOOK_DIR/

WORKDIR /home
ENTRYPOINT  $ZEPPELIN_HOME/bin/zeppelin-daemon.sh start  && bash

#RUN curl -s http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.6.tgz | tar -xz -C /usr/local/
#RUN cd /usr/local && ln -s spark-2.1.0-bin-hadoop2.6 spark
#ENV SPARK_HOME /usr/local/spark


#RUN mkdir $SPARK_HOME/yarn-remote-client
#ADD yarn-remote-client $SPARK_HOME/yarn-remote-client

#RUN $BOOTSTRAP && $HADOOP_PREFIX/bin/hadoop dfsadmin -safemode leave && $HADOOP_PREFIX/bin/hdfs dfs -put $SPARK_HOME-2.1.0-bin-hadoop2.6/lib /spark
#
#ENV YARN_CONF_DIR $HADOOP_PREFIX/etc/hadoop
#ENV PATH $PATH:$SPARK_HOME/bin:$HADOOP_PREFIX/bin
## update boot script
#COPY bootstrap.sh /etc/bootstrap.sh
#RUN chown root.root /etc/bootstrap.sh
#RUN chmod 700 /etc/bootstrap.sh


#ENTRYPOINT ["/etc/bootstrap.sh"]