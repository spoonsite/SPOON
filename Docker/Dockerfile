################
## Base Image ##
################

FROM tomcat:7-jre8-alpine

MAINTAINER "Kent Bair <kent.bair@sdl.usu.edu>"

RUN apk update
RUN apk upgrade
RUN apk add curl libc6-compat

###################
## ElasticSearch ##
###################

ENV ES_NAME elasticsearch
ENV ES_HOME /usr/local/share/$ES_NAME
ARG ES_VERSION=2.4.3
ARG ES_TGZ_URL=https://download.elastic.co/$ES_NAME/release/org/$ES_NAME/distribution/tar/$ES_NAME/$ES_VERSION/$ES_NAME-$ES_VERSION.tar.gz

RUN mkdir -p "$ES_HOME" \
	&& chmod 755 -R "$ES_HOME"

RUN addgroup $ES_NAME

RUN adduser -Ss /bin/sh -h $ES_HOME -G $ES_NAME $ES_NAME 

WORKDIR $ES_HOME

RUN set -x \
	&& curl -fSL "$ES_TGZ_URL" -o $ES_NAME.tar.gz \
	&& tar -zxvf $ES_NAME.tar.gz --strip-components=1 \
	&& rm $ES_NAME.tar.gz* \
	&& chown -R $ES_NAME:$ES_NAME $ES_HOME


ENV CATALINA_OPTS -Xmx2048m

WORKDIR $CATALINA_HOME/webapps

################
## StoreFront ##
################

ARG BUILD=FALSE
ARG STOREFRONT_HOME=/usr/local/share/openstorefront
ARG STOREFRONT_VERSION=2.0
ARG STOREFRONT_WAR_URL=https://github.com/di2e/openstorefront/releases/download/v$STOREFRONT_VERSION/openstorefront.war

# Switching between development and production must be done manually
#
# To switch, uncomment the RUN line and comment out the COPY line below
# Be sure to change the version so that the appropriate WAR file is downloaded
#
# The copy line pulls in the WAR file from the currently working set of directories
# (It should only be used locally during development when the WAR file can be built first)
# (Be sure to build the WAR file first before building the docker image)

RUN if [ "$BUILD" = "TRUE" ]; then echo "build not configured"; fi 
RUN if [ "$BUILD" = "FALSE" ]; then curl -fSL "$STOREFRONT_WAR_URL" -o openstorefront.war; fi

#COPY server/openstorefront/openstorefront-web/target/openstorefront.war $CATALINA_HOME/webapps/ROOT.war

####################
## Startup Script ##
####################

RUN mkdir -p "$STOREFRONT_HOME" \
	&& chmod 755 -R "$STOREFRONT_HOME"

WORKDIR $STOREFRONT_HOME

COPY upgrade.sh upgrade.sh
COPY startup.sh startup.sh
COPY tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml

RUN chmod +x upgrade.sh startup.sh

RUN echo "STOREFRONT_WAR_URL = $STOREFRONT_WAR_URL"
RUN echo "BUILD = $BUILD"


####################
## Start Services ##
####################

ENTRYPOINT ./startup.sh 
