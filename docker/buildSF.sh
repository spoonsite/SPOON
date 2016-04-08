#!/bin/bash

#Build each layerA

echo 'Removing all docker images....building clean'
echo -en '\n\n'

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi $(docker images -q)
echo -en '\n\n'
echo "Docker images, containers removed"

#Build Java Image
./buildJava.sh
#Build Solr Image on top
./buildSolr.sh
#Build Tomcat Image on top
./buildTomcat.sh
#Build Storefront Image on Too
./buildFinal.sh
