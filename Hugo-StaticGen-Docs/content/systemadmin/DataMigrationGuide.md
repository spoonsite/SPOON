+++
title = "Data Migration Guide"
description = ""
weight = 5
+++

This guide provide help for importing data from external sources as well as moving data from one instance to another instance.

<!--more-->

## Importing/Export (Third parties)

The application provides set of standard format that it accepts to import Attributes/Categories as well as Entries. It also provides a set of custom formats that can have mappings applied. These custom formats allow taking a variety of data with so simple handling.

See **Admin Tools** -> **Data Management** -> **Imports**

### Custom Mapping

{{% notice warning %}}
This feature is in beta.
{{% /notice %}}

An admin can create field mapping from a file field to a field in application. Some pre-defined transformation can be applied such as lowercasing the field.

In some cases, field mapping is insufficient to import a source. A plugin can be created to provide more handling control (see Developer Guide).

## System Archives (System to System / Restore)

All system exports are handled through MongoDB. See the documentation on how to import and export the database: [mongoimport](https://docs.mongodb.com/manual/reference/program/mongoimport/) and [mongoexport](https://docs.mongodb.com/manual/reference/program/mongoexport/).

## Full Data Export and Backup

To get a backup of the Mongo database use the command line tools `mongodump` and `mongorestore`. See https://www.digitalocean.com/community/tutorials/how-to-back-up-restore-and-migrate-a-mongodb-database-on-ubuntu-14-04 for a guide.

Doing a backup of the Mongo database only backs up the data. There are still resources such as images, pdfs, and documents that also need to be backed up. In this case the application data directory will need to be copied from the file system and backed up.

## Make backup of the database

This assumes you have a database named "storefront" in your database.

```sh
mkdir mongo-backup
mongodump --db storefront --out mongo-backup/`date +"%m-%d-%y"`
```

To get the rest of the files for the application go to the data location when the server was setup. For example `/var/spoon/`. And then backup the data directory.

```sh
cd /var/
zip -r backup_`date +"%m-%d-%y"`.zip spoon
```

### Restore backup

```sh
cd 01-08-20/
mongorestore --db <restored database> --drop storefront 
```

### Restore the backup on a docker container

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
