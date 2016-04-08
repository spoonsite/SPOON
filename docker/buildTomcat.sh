#!/bin/bash

echo -en '\n\n'
echo "Building Tomcat Image"
echo -en '\n\n'
docker build -t razaltan/storefront-tomcat --file ./Dockerfile-tomcat .
docker images
echo -en '\n\n'
echo "Completed Tomcat Image"

