+++
title = "Setup"
description = ""
weight = 2
markup = "mmark"
+++

## Walkthrough

This Documentation will walk you through setting up a production server to serve SPOON.

- [Prerequisites](http://localhost:1313/systemadmin/setup/#prerequisites)
- [OpenJDK](http://localhost:1313/systemadmin/setup/#openjdk)
- [Elasticsearch](http://localhost:1313/systemadmin/setup/#elasticsearch)
- [MongoDB](http://localhost:1313/systemadmin/setup/#mongodb)
- [Tomcat](http://localhost:1313/systemadmin/setup/#tomcat)
- [Deployment](http://localhost:1313/systemadmin/setup/#deployment)

### Prerequisites

Required Hardware (can be run on a VM or dedicated hardware):

Suggested:

- CPUs: 4
- RAM: 8GB
- DISK: 40GB (Increase, if storing a lot of media and resources locally)

Minimum:

- CPUs: 1
- RAM: 2GB (Application should be set to use 1GB)
- DISK: 20GB

Required Software:

- OpenJDK 8
- Elasticsearch 7.2.1
- MongoDB 4.2.0
- Tomcat 7.0.96

### Server Setup

Install some helpful tools:

- sudo (admin access)
- vim (file editing)

```sh
yum install -y sudo vim wget
```

WARING: Some commands may need to be run with sudo.

#### OpenJDK

1. Install openjdk-8:

    ```sh
    yum install java-1.8.0-openjdk
    java -version
    ```

    Verify that the openjdk version is '1.8.0_***'

#### Elasticsearch

Get and install elasticsearch:

```sh
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm.sha512
sha512sum -c elasticsearch-7.2.1-x86_64.rpm.sha512
rpm --install elasticsearch-7.2.1-x86_64.rpm
```

Enable elasticsearch as a service and start elasticsearch

```sh
chkconfig elasticsearch on
service elasticsearch start
service elasticsearch status
```

Verify that elasticsearch is running, the last command from above should output something similar to:

```sh
elasticsearch (pid  556) is running...
```

#### MongoDB

#### Tomcat

### Deployment
