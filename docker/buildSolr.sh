#!/bin/bash

echo -en '\n\n'
echo "Building Solr Image"
echo -en '\n\n'
docker build -t razaltan/storefront-solr --file ./Dockerfile-solr .
docker images
echo -en '\n\n'
echo "Completed Solr Image"

