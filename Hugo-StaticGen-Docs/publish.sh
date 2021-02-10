#!/bin/bash

# assumes spoonsite.github.io has also been cloned
DEPLOY="../../spoonsite.github.io"
DATE=$(date)

echo "building site"
rm -r public/*
hugo

echo "cleaning out old site"
rm -r $DEPLOY/*

echo "copying build to $DEPLOY"
cp -r public/* $DEPLOY

cd $DEPLOY
git add -A
git commit -m "Build on $DATE"
git push