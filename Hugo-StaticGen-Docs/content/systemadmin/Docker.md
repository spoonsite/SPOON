+++
title = "Docker"
description = ""
weight = 3
+++

Information on running SPOON in a Docker container.

<!--more-->

The SPOON application contains a Dockerfile in order to build Docker images. There are two Dockerfiles in the repo. One is for development which is used by Jenkins to deploy the application for testing of new features. The other dockerfile is for the release build. These can be found in the repo on [GitHub](https://github.com/spoonsite/spoon/tree/master/Docker).

SPOON also automatically builds release versions of the application for consumption by users. These builds are available on [Docker Hub](http://hub.docker.com).

## Development Docker Container

To build the container you will need:

- a zip of an SPOON database
- the startup.sh
- the tomcat-users.xml

The `startup.sh` and `tomcat-users.xml` can be found at [GitHub](https://github.com/di2e/openstorefront/tree/master/Docker/develop). To obtain the database take the database folder from a previous running instance of Storefront and zip the folder. Place all three items in the same directory as the dockerfile when building the image. To build the image run:

```bash
docker build -t some-tag .
```

To run the container you will need a war file to mount to the container.

Run the container as a detached process with:

```bash
docker run -d \
-v $(pwd)/openstorefront.war:/usr/local/tomcat/webapps/openstorefront.war \
-p 8081:8080 \
openstorefront
```

Since there is already a dataset built into the container, in order to run the application on an empty database point the database location to a different location with:

```bash
docker run -d \
-v $(pwd)/openstorefront.war:/usr/local/tomcat/webapps/openstorefront.war \
-p 8081:8080 \
--env CATALINA_OPTS="-Xmx2048m -Dapplication.datadir=/var/selenium" \
openstorefront
```
