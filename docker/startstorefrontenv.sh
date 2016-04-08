#!/bin/bash

set -e

echo '######Starting Solr#####'
cd /opt/solr/example
java -jar start.jar &

sleep 10

echo '######Solr Done######'
echo ''
echo '######Starting Tomcat###'
cd /usr/local/tomcat/bin
./catalina.sh run 
sed -i -e 's#solr.server.url=http://localhost:8983/solr/esa#solr.server.url=http://localhost:8983/solr/collection1#g' '/var/openstorefront/config/openstorefront.properties'

echo '######Tomcat Done ######'
