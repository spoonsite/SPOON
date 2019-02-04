# About the Dockerfiles

To build the container you will need:

- a zip of an openstorefront database
- the startup.sh
- the tomcat-users.xml

To run the container you will need:

- a war file to mount to the container

Run the container as a detached process with:

```
docker run -d \
-v $(pwd)/openstorefront.war:/usr/local/tomcat/webapps/openstorefront.war \
-p 8081:8080 \
openstorefront
```

Since there is already a dataset build into the container, in order to run the application on an empty database point the database location to a different location with:

```
docker run -d \
-v $(pwd)/openstorefront.war:/usr/local/tomcat/webapps/openstorefront.war \
-p 8081:8080 \
--env CATALINA_OPTS="-Xmx2048m -Dapplication.datadir=/var/selenium" \
openstorefront
```