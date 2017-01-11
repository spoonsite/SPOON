################
## Base Image ##
################

FROM centos:7

################
## Maintainer ##
################

MAINTAINER flammablefork

##########
## Java ##
##########

RUN yum update -y \
  && yum -y install unzip java-1.8.0-openjdk-devel \
  && yum clean all

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0
ENV PATH "$PATH":/${JAVA_HOME}/bin:.:

###################
## ElasticSearch ##
###################

ENV ES_HOME /usr/local/share/elasticsearch
ENV ES_NAME elasticsearch
ENV ES_VERSION 5.0.2
ENV ES_TGZ_URL https://artifacts.elastic.co/downloads/elasticsearch/$ES_NAME-$ES_VERSION.tar.gz

RUN mkdir -p "$ES_HOME" \
	&& chmod 755 -R "$ES_HOME"

RUN useradd -rU $ES_NAME -d $ES_HOME

WORKDIR $ES_HOME

RUN set -x \
	&& curl -fSL "$ES_TGZ_URL" -o $ES_NAME.tar.gz \
	&& tar -zxvf $ES_NAME.tar.gz --strip-components=1 \
	&& rm $ES_NAME.tar.gz* \
	&& chown -R $ES_NAME:$ES_NAME $ES_HOME

############
## Tomcat ##
############

ENV CATALINA_HOME /usr/local/share/tomcat
ENV TOMCAT_MAJOR 7
ENV TOMCAT_PORT 8080
ENV TOMCAT_VERSION 7.0.73
ENV TOMCAT_TGZ_URL https://www.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
ENV CATALINA_OPTS -Xmx2048m

RUN mkdir -p "$CATALINA_HOME" \
	&& chmod 755 -R "$CATALINA_HOME" 

WORKDIR $CATALINA_HOME

RUN set -x \
	&& curl -fSL "$TOMCAT_TGZ_URL" -o tomcat.tar.gz \
	&& tar -zxvf tomcat.tar.gz --strip-components=1 \
	&& rm tomcat.tar.gz* \
	&& rm -rf webapps/*
	
EXPOSE $TOMCAT_PORT

################
## StoreFront ##
################

COPY server/openstorefront/openstorefront-web/target/openstorefront.war $CATALINA_HOME/webapps

####################
## Startup Script ##
####################

ENV STORE_HOME /usr/local/share/openstorefront

RUN mkdir -p "$STORE_HOME" \
	&& chmod 755 -R "$STORE_HOME"

WORKDIR $STORE_HOME

RUN echo runuser -l $ES_NAME -c \"$ES_HOME/bin/$ES_NAME -d\" > startup.sh && \
    echo $CATALINA_HOME/bin/catalina.sh run >> startup.sh

RUN chmod +x startup.sh

####################
## Start Services ##
####################

ENTRYPOINT startup.sh