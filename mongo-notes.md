# Running MongoDB for StoreFront


See https://docs.docker.com/samples/library/mongo/ for container information.

## Development and Testing

For ease of use just bind the port to `0.0.0.0` with the  `mongod.conf` file.

```yaml
# mongod.conf
net:
   bindIp: 0.0.0.0
   port: 27017
```

If you are having issues using the default mongo container build a container with the following Dockerfile:

```Dockerfile
FROM mongo:4.2.0-bionic

RUN mkdir -p /etc/mongo

RUN echo "net:\n    bindIp: 0.0.0.0\n    port: 27017" > /etc/mongo/mongod.conf
```

To build: `docker build -t spoon-mongo .`
To run: `docker run -it --name some-mongo -p 27017:27017 spoon-mongo --config /etc/mongo/mongod.conf`

Application settings need to be changed to allow mongo to run: 

```ini
db.use.mongo=true
mongo.connection.url=mongodb://172.31.24.33
mongo.database=storefront
```

These can be changed in the application and also need to be changed in the openstorefront.properties file

A Mongo inspector tool is nice to have, the following have been tested and work:
   - Robo 3T
   - NoSQLBooster for MonogDB

Run the docker container with 

```sh
docker run --name some-mongo -v /my/custom:/etc/mongo -d mongo --config /etc/mongo/mongod.conf
```

Make sure you have `mongod.conf` in `/my/custom` folder above.


## Deployment

You will want a more secure setup for Mongo in production.

TODO: Setup configs for MongoDB deployment