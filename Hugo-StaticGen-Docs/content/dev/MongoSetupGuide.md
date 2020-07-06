+++
title = "MongoDB Setup Guide"
description = "MongoDB Setup Guide"
weight = 2
markup="mmark"
+++

This guide describes how to setup MongoDB in a docker container or install locally for development.
<!--more-->

## Method #1: Build a Docker Container with a Dockerfile

Create a "Dockerfile" with the following contents:

```Dockerfile
FROM mongo:4.2.0-bionic

RUN mkdir -p /etc/mongo

RUN echo "net:\n    bindIp: 0.0.0.0\n    port: 27017" > /etc/mongo/mongod.conf
```

{{% notice warning %}}
Don't make a mongod.conf like this for production. `bindIp: 0.0.0.0` allows connections from anywhere and is not secure.
{{% /notice %}}

This docker file must be all by itself inside of its own directory.

Now in the directory containing this file we need to run the following:

```sh
docker build -t spoon-mongo .

docker run -it --name some-mongo -p 27017:27017 spoon-mongo --config /etc/mongo/mongod.conf
```

Finally you need to change some property lines inside of the openstorefront.properties file of your database directory.
For example, the openstorefront.properties file might be found in: `C:\dev\SPOON_DATA\config`
The following properties need to be added near the top of the file.

```ini
db.use.mongo=true
mongo.database=storefront
```

Now that you have performed all the above steps, you just need to shutdown your tomcat server and restart it along with elasticsearch. After a few minutes your spoonsite instance should be up and running along with all the data found in the aforementioned directory.

## Method #2: Mount the mongod.conf file into the container

First you need to make a mongod.conf file and give it the following contents:

```yaml
# mongod.conf
net:
   bindIp: 0.0.0.0
   port: 27017
```

Now that you have that file made, you need to take note of its parent directory location on disk.
For example, the directory containing the file could be at `C:\dev\tempdir\`.

Now you need to run the following command:

```sh
docker run --name some-mongo -v C:\dev\tempdir\:/etc/mongo -d mongo --config /etc/mongo/mongod.conf
```

You may have to resolve the issues with the choice of forward slashes and/or backslashes.

Also remember you will still need to modify the openstorefront.properties file as mentioned above. 
Further you may need to add an additional property to the file:

```ini
db.use.mongo=true
mongo.connection.url=mongodb://<your ip address>
mongo.database=storefront
```

Further a Mongo inspector tool is nice to have, the following have been tested and work:

- Robo 3T
- NoSQLBooster for MonogDB

Finally, should you want for more documentation see https://docs.docker.com/samples/library/mongo/


## Method #3: MongoDB installed to local machine
   - Install mongo to your local machine: https://docs.mongodb.com/manual/installation/
   - Ensure the mongodb server is running; Default configuration will run on port 27017
   - OPTIONAL: connect MongoDBCompass to the server for visualizing the database with `mongodb:\\127.0.0.1:27017`
   - Windows: To open a mongo shell, run `\Mongo_install_path\Server\<version>\bin\mongo`
      - Check that the server is successfully connected
      - The database has been created if the following command shows 'openstorefront' as non-zero:
         ```sh
         show dbs
         ```

## Restore data to the database

You should have a backup of the database.

### Windows
```sh
mkdir \location\for\backup-dir
cd \Mongo_install_path\Server\<version>\bin\
.\mongodump.exe --db storefron --out \path\to\backup-dir\"backup name"
```

### Linux/Unix
```sh
mkdir mongo-backup
mongodump --db storefront --out mongo-backup/`date +"%m-%d-%y"`
```

And then put that data inside the running docker container (or restore locally).

```sh
# copy the backup to the container
docker cp 01-08-20/. <mongo container>:/home

# get a shell to the running container
docker exec -it <mongo container> bash

# now the shell is inside the docker container
cd /home


# restore the backup "storefront" database
mongorestore --db <restored database> --drop storefront
```

See the [Mongo data migration guide]({{< ref "DataMigrationGuide.md" >}})
