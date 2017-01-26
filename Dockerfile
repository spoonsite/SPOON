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

ENV ES_NAME elasticsearch
ENV ES_HOME /usr/local/share/$ES_NAME
ENV ES_VERSION 2.4.3
ENV ES_TGZ_URL https://download.elastic.co/$ES_NAME/release/org/$ES_NAME/distribution/tar/$ES_NAME/2.4.3/$ES_NAME-$ES_VERSION.tar.gz

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
ENV TOMCAT_VERSION 7.0.75
ENV TOMCAT_TGZ_URL http://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
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

ENV STOREFRONT_HOME /usr/local/share/openstorefront
ENV STOREFRONT_VERSION 2.2
ENV STOREFRONT_WAR_URL https://github.com/di2e/openstorefront/releases/download/v$STOREFRONT_VERSION/openstorefront.war

WORKDIR $CATALINA_HOME/webapps

# Switching between development and production must be done manually
#
# To switch, uncomment the RUN line and comment out the COPY line below
# Be sure to change the version so that the appropriate WAR file is downloaded
#
# The copy line pulls in the WAR file from the currently working set of directories
# (It should only be used locally during development when the WAR file can be built first)
# (Be sure to build the WAR file first before building the docker image)

RUN curl -fSL "$STOREFRONT_WAR_URL" -o ROOT.war

#COPY server/openstorefront/openstorefront-web/target/openstorefront.war $CATALINA_HOME/webapps/ROOT.war

####################
## Startup Script ##
####################

RUN mkdir -p "$STOREFRONT_HOME" \
	&& chmod 755 -R "$STOREFRONT_HOME"

WORKDIR $STOREFRONT_HOME

RUN echo "#!/bin/bash" > upgrade.sh && \
    echo "" >> upgrade.sh && \
	echo "for i in \"\$@\"" >> upgrade.sh && \
	echo "do" >> upgrade.sh && \
	echo "case \$i in" >> upgrade.sh && \
	echo "    --to-version=*)" >> upgrade.sh && \
	echo "    URL=$STOREFRONT_WAR_URL" >> upgrade.sh && \
	echo "    curl -fSL \"\${URL/$STOREFRONT_VERSION/\${i#*=}}\" -o $CATALINA_HOME/webapps/\${i#*=}.war" >> upgrade.sh && \
	echo "    " && \
	echo "    if [ -f \"$CATALINA_HOME/webapps/\${i#*=}.war\" ]; then" >> upgrade.sh && \
	echo "        " >> upgrade.sh && \
	echo "        $CATALINA_HOME/bin/catalina.sh stop" >> upgrade.sh && \
	echo "        rm -rf $CATALINA_HOME/webapps/ROOT.war $CATALINA_HOME/webapps/ROOT/" >> upgrade.sh >> upgrade.sh && \
	echo "        mv $CATALINA_HOME/webapps/\${i#*=}.war $CATALINA_HOME/webapps/ROOT.war" >> upgrade.sh && \
	echo "        $CATALINA_HOME/bin/catalina.sh run" >> upgrade.sh && \
	echo "    fi" >> upgrade.sh && \
	echo "    " >> upgrade.sh && \
	echo "    shift" >> upgrade.sh && \
	echo "    ;;" >> upgrade.sh && \
	echo "    *)" >> upgrade.sh && \
	echo "    ;;" >> upgrade.sh && \
	echo "esac" >> upgrade.sh && \
	echo "done" >> upgrade.sh && \
	echo "" >> upgrade.sh

RUN echo "#!/bin/bash" > startup.sh && \
    echo "" >> startup.sh && \
    echo "runuser -l $ES_NAME -c \"$ES_HOME/bin/$ES_NAME -d\"" >> startup.sh && \
    echo "$CATALINA_HOME/bin/catalina.sh run" >> startup.sh && \
	echo "" >> startup.sh

RUN chmod +x upgrade.sh startup.sh

####################
## Start Services ##
####################

ENTRYPOINT startup.sh