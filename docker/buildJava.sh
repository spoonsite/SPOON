#!/bin/bash

echo -en '\n\n'
echo "Building Java Image"
echo -en '\n\n'
docker build -t razaltan/storefront-java --file ./Dockerfile-java .
docker images
echo -en '\n\n'
echo "Completed Java Image"

