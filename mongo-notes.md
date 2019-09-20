# Running MongoDB for StoreFront


See https://docs.docker.com/samples/library/mongo/ for container information.

## Development and Testing

For ease of use just bind the port to `0.0.0.0` with the  `mongod.conf` file.

```ini
# mongod.conf
net:
   bindIp: 0.0.0.0
   port: 27017
```

Run the docker container with 

```sh
docker run --name some-mongo -v /my/custom:/etc/mongo -d mongo --config /etc/mongo/mongod.conf
```

Make sure you have `mongod.conf` in `/my/custom` folder above.


## Deployment

You will want a more secure setup for Mongo in production.

TODO: Setup configs for MongoDB deployment