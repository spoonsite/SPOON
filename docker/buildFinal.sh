#!/bin/bash

echo -en '\n\n'
echo "Building Final Storefront Image"
echo -en '\n\n'
docker build -t razaltan/storefront --file ./Dockerfile-final .
docker images
echo -en '\n\n'
echo "Completed Final Image"

