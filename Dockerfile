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
ENV ES_VERSION 2.4.3
ENV ES_TGZ_URL https://download.elastic.co/$ES_NAME/release/org/$ES_NAME/distribution/tar/$ES_NAME/2.4.3/$ES_NAME-$ES_VERSION.tar.gz

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


################
## StoreFront ##
################

RUN echo -e "<?xml version='1.0' encoding='utf-8'?>\n" \
"<tomcat-users>" \
"        <role rolename=\"manager-gui\"/>\n" \
"        <role rolename=\"manager-gui\"/>\n" \
"        <role rolename=\"manager-script\"/>\n" \
"        <user username=\"admin\" password=\"Secret1@\" roles=\"manager,manager-gui,manager-script\" />\n" \
"</tomcat-users>\n" > $CATALINA_HOME/conf/tomcat-users.xml 

ENV STOREFRONT_HOME /usr/local/share/openstorefront
ENV STOREFRONT_VERSION 2.0
ENV STOREFRONT_WAR_URL https://github.com/di2e/openstorefront/releases/download/v$STOREFRONT_VERSION/openstorefront.war

ENV CATALINA_OPTS -Xmx2048m

WORKDIR $CATALINA_HOME/webapps

# Switching between development and production must be done manually
#
# To switch, uncomment the RUN line and comment out the COPY line below
# Be sure to change the version so that the appropriate WAR file is downloaded
#
# The copy line pulls in the WAR file from the currently working set of directories
# (It should only be used locally during development when the WAR file can be built first)
# (Be sure to build the WAR file first before building the docker image)

RUN curl -fSL "$STOREFRONT_WAR_URL" -o openstorefront.war

#COPY server/openstorefront/openstorefront-web/target/openstorefront.war $CATALINA_HOME/webapps/ROOT.war

####################
## Startup Script ##
####################

RUN mkdir -p "$STOREFRONT_HOME" \
	&& chmod 755 -R "$STOREFRONT_HOME"

WORKDIR $STOREFRONT_HOME


RUN echo -e '#!/bin/sh \n' \
            "\n" \
            "for i in \"\$@\" \n" \
            "do \n" \
            "case \$i in \n" \
            "    --to-version=*) \n" \
            "    \n" \
            "    $CATALINA_HOME/bin/catalina.sh stop 30 \n" \
            "    \n" \
            "    URL=$STOREFRONT_WAR_URL \n" \
            "    curl -fSL \"\${URL/$STOREFRONT_VERSION/\${i#*=}}\" -o $CATALINA_HOME/webapps/\${i#*=}.war \n" \
            "    \n" \
            "    if [ -f \"$CATALINA_HOME/webapps/\${i#*=}.war\" ]; then \n" \
            "        \n" \
            "        rm -rf $CATALINA_HOME/webapps/ROOT.war $CATALINA_HOME/webapps/ROOT/ \n" \
            "        mv $CATALINA_HOME/webapps/\${i#*=}.war $CATALINA_HOME/webapps/ROOT.war \n" \
            "    fi \n" \
            "    \n" \
            "    $CATALINA_HOME/bin/catalina.sh start \n" \
            "    \n" \
            "    shift \n" \
            "    ;; \n" \
            "    *) \n" \
            "    ;; \n" \
            "esac \n" \
            "done \n" \
            "\n" \
            "echo -e ' \n" \
            "  Program has completed. \n" \
            "  If you upgraded, please wait a few moments for the new version to initialize. \n" \
            "  \n" \
            "  You may exit the terminal by entering \"exit\" (without quotes) and pressing Enter. \n" \
	    "  '" > upgrade.sh

RUN echo -e '#!/bin/sh \n' \
            "\n" \
            "su - $ES_NAME -c \"$ES_HOME/bin/$ES_NAME -d\" \n" \
            "$CATALINA_HOME/bin/catalina.sh start \n" \
	        "\n" \
	        "tail -f $CATALINA_HOME/logs/catalina.out \n" > startup.sh

RUN chmod +x upgrade.sh startup.sh

####################
## Start Services ##
####################

ENTRYPOINT ./startup.sh 
