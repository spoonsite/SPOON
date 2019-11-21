# So you want to start up a docker container that is using mongo and then be able to connect to it?

## Here we present the primary method for standing up a mongo db instance for developers

### METHOD #1 Build a Docker Container with a Dockerfile

1. First we need to create a file called "Dockerfile" and it has to have the following contents:

```Dockerfile
FROM mongo:4.2.0-bionic

RUN mkdir -p /etc/mongo

RUN echo "net:\n    bindIp: 0.0.0.0\n    port: 27017" > /etc/mongo/mongod.conf
```

This docker file must be all by itself inside of its own directory.

1. Now in the directory containing this file we need to run the following commands:

```sh
docker build -t spoon-mongo .
```

Followed by:

```sh
docker run -it --name some-mongo -p 27017:27017 spoon-mongo --config /etc/mongo/mongod.conf
```

1. Finally you need to change some property lines inside of the openstorefront.properties file of your database directory.
For example, the openstorefront.properties file might be found in: "C:\dev\...\SPOON_DEAD_STAGING\config"
The following properties need to be added near the top of the file.

```ini
db.use.mongo=true
mongo.database=storefront
```

1. Now that you have performed all the above steps, you just need to shutdown your tomcat server and restart it along with elasticsearch. After a few minutes your
spoonsite instance should be up and running along all the data found in the aforementioned directory.

## Here we show a secondary method to standing up a mongo instance, although this has had limited success

### METHOD #2 Mount the mongod.conf file into the container

1. First you need to make a mongod.conf file and give it the following contents:

```yaml
# mongod.conf
net:
   bindIp: 0.0.0.0
   port: 27017
```

1. Now that you have that file made, you need to take note of its parent directory location on disk.
For example, the directory containing the file could be at "C:\dev\tempdir\".

1. Now you need to run the following command:

```sh
docker run --name some-mongo -v C:\dev\tempdir\:/etc/mongo -d mongo --config /etc/mongo/mongod.conf
```

You may have to resolve the issues with the choice of forward slashes and/or backslashes.

1. Also remember you will still need to modify the openstorefront.properties file as mentioned above. 
Further you may need to add an additional property to the file:

```ini
db.use.mongo=true
mongo.connection.url=mongodb://<your ip address>
mongo.database=storefront
```

1. Further a Mongo inspector tool is nice to have, the following have been tested and work:
   - Robo 3T
   - NoSQLBooster for MonogDB

1. Finally, should you want for more documentation:

Please see https://docs.docker.com/samples/library/mongo/ for container information.

## Deployment

You will want a more secure setup for Mongo in production.

TODO: Setup configs for MongoDB deployment