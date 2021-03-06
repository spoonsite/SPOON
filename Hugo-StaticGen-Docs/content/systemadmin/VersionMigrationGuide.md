+++
title = "Version Migration"
description = ""
weight = 6
markup = "mmark"
+++

This guide details changes and upgrades that have happened from version to version.

<!--more-->

In general, you should always upgrade one version at a time in order (i.e., going from 2.2 to 2.4, then 2.2 to 2.3, then to 2.4).This will ensure data migrations will occur in the proper order. If you start from the latest version and have no data then migration is not needed.

## Note: v2.11

Mongo DB support was added. Orient is still used the default database unless configured otherwise.

1. Install MongoDB 4.2.1. This can be done through `yum`. See the [MongoDB documentation](https://docs.mongodb.com/v4.2/tutorial/install-mongodb-on-red-hat/)

1. Add/edit application properties

    ```ini
    db.use.mongo=true
    mongo.connection.url=mongodb://localhost:27017
    ```

1. Either deploy 2.11 or, if already deployed, then restart the server for the change to take effect

    The data migration process will apply automatically if the following conditions are met:

    1. Configured to use Mongo

    1. There was an previous Orient Install (as determined by the db file directory)

    1. If the migration hasn't already been applied

    To force it to re-apply remove the `DB-MIGRATION-Mongo_STATUS, DB-MIGRATION-Mongo_LASTRUN_DTS` Application Properties
    from the database by using the application system UI or by using an external tool.

    Generally the migration process is expected to only take about 1-2 minutes, but it depends on the size of database and system resources.

{{% notice warning %}}
The data migration will remove existing Mongo "Storefront" database collections and replace the content from the equivalent
Orient data. Orient's data is not affected by the migration, so the process can be repeated.
{{% /notice %}}

## Note: v2.10

This version requires an upgrade to Elasticsearch 7.2.1. This can be done through an RPM. See the
[Elasticsearch documentation](https://www.elastic.co/guide/en/elasticsearch/reference/7.2/rpm.html).

## Note: v2.6.3

For existing applications, update **var/openstorefront/config/shiro.ini**.

Add:

```ini
/login/** = anon
/mobile/** = authc
/api/v1/resource/branding/current = anon
/api/v1/resource/securitypolicy = anon
/api/v1/resource/faq/* = anon
/api/v1/resource/faq = anon
/api/v1/resource/feedbacktickets = anon
```

## Note: v2.6

Some features such as custom submission require new permissions (See **Admin** -> **Security Roles** to add new permissions).

## Note: v2.4.3

This includes the database upgrade. Note: Rollback to previous storefront version will require a restore from a backup prior to 2.4.3 for existing data to work for that version.

**2.4.2** must be deployed before 2.4.3 if you have existing data.

## Note: v2.4.2

Includes a database export and import in preparation for 2.4.3 upgrade of database. If you have an existing database, install 2.4.2 before 2.4.3.

1. Shutdown app server
2. Backup /var/openstorefront/db directory
3. Install/deploy 2.4.2
4. Start app server

You need to wait for 5-20 minutes (depending on size of DB) for the update to occur. During this time the application will be unavailable.

To manually export/import:

1. Install Orient 2.1.25 to system to use console tool
    1. Shut down app server

From Orient

```bash
$ cd bin
>
$ ./console.sh (or bin/console.bat under Windows)
orientdb> CONNECT plocal:/var/openstorefront/db/openstorefront <DBuser> <db password
orientdb> EXPORT DATABASE /temp/mydb.json.gz
orientdb> DISCONNECT
orientdb> CREATE DATABASE plocal:/var/openstorefront/db/openstorefront (only do this if you have move the old one out of the way)
orientdb> IMPORT DATABASE /temp/mydb.json.gz
```

## Upgrading from v2.4 to v2.5

Pre-Deployment

### ElasticSearch

Change the value of **elastic.server.port** in /var/openstorefront/config/**openstorefront.properties** from **9300** to **9200**

Windows (manually)

1. Install and extract Elasticsearch 5.6.3 on the system
2. Remove/delete the root directory of Elasticsearch 2.x from the system
3. Start Elasticsearch 5.6.3 (by running the elasticsearch-5.6.3/elasticsearch-5.6.3/elasticsearch.bat file)

CentOS using Yum

_Documentation on installation:_
_[https://www.elastic.co/guide/en/elasticsearch/reference/5.6/rpm.html](https://www.elastic.co/guide/en/elasticsearch/reference/5.6/rpm.html)_

1. Download and install the public signing key

```bash
rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
```

1. Add the following in your /etc/yum.repos.d/ directory in a file with a .repo suffix, for example /etc/yum.repos.d/elasticsearch.repo

```ini
[elasticsearch-6.x]
name=Elasticsearch repository for 6.x packages
baseurl=https://artifacts.elastic.co/packages/6.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
```

1. Install Elasticsearch

```bash
sudo yum install elasticsearch
```

1. Run Elasticsearch (with **SysV init**)

```bash
# on system boot
sudo chkconfig --add elasticsearch

# manually
sudo -i service elasticsearch start
sudo -i service elasticsearch stop
```

1. Run Elasticsearch (with **systemd**)

```bash
# on system boot
sudo /bin/systemctl daemon-reload
sudo /bin/systemctl enable elasticsearch.service

#manually
sudo systemctl start elasticsearch.service
sudo systemctl stop elasticsearch.service
```

Elasticsearch service command reference (Elasticsearch should be running at this point):

**Start:** systemctl start elasticsearch.service

**Stop:** systemctl stop elasticsearch.service

**Test:** curl -XGET 'localhost:9200/?pretty'

should give you a response something like this:

```json
{
  "name": "Cp8oag6",
  "cluster_name": "elasticsearch",
  "cluster_uuid": "AT69_T_DTp-1qgIJlatQqA",
  "version": {
    "number": "5.4.3",
    "build_hash": "f27399d",
    "build_date": "2016-03-30T09:51:41.449Z",
    "build_snapshot": false,
    "lucene_version": "6.5.0"
  },
  "tagline": "You Know, for Search"
}
```

Getting everything setup in SPOON:

1. In Openstorefront, navigate to **Admin Tools** -> **Application Management** -> **System** -> **Search Control**
2. Click "Re-Index Listings"
3. (optional) If you haven't set the elastic.server.port to 9200, you can do so now from **Admin Tools** -> **App Management**
-> **System** -> **System Configuration Properties**

## Upgrading from v2.3 to v2.4

{{% notice note %}}
As part of the upgrade, Metadata will be automatically converted to Attributes. This may take a while if there is a lot of
Metadata associated with the entries. The server won't be available until the migration is complete.
{{% /notice %}}

Pre-Deployment

- Edit /var/openstorefront/config/shiro.ini
- Under [URL] section:
  Confirm line "/images" is
  `/images/* = anon`
- If not then update it
- Then restart server if it's running to apply changes

## Upgrading from v2.2 to v2.3

DI2E environments that use OpenAM should follow Jira ticket STORE-1243.

1. **Update Security** - If you haven't done any customization then the easiest upgrade path is to just remove the existing shiro.ini.
    1. Delete /var/openstorefront/config/shiro.ini
    1. On next server restart, the application will pull the default

Keep in mind this is for environments that use the built in user management rather than an external user management. The default shiro
config is set for the built in user management.

1. **Update Database** - 2.3 includes an update to the database.
    1. Make sure Tomcat is shutdown
    1. Make a backup of existing db

1. Copy /var/openstorefront/db directory to backup location
    1. Delete all /var/openstorefront/db/databases/openstorefront/openstorefront.\*.wal files (just the WAL files, of which there may be
        one or more; this appears to be optional)
