+++
title = "Setup"
description = ""
weight = 2
markup = "mmark"
+++


These are instructions on how to get production instance of SPOON set up on a VM.
<!--more-->

## Walkthrough

This Documentation will walk you through setting up a production server to serve SPOON.

- [Prerequisites](/systemadmin/setup/#prerequisites)
- [OpenJDK](/systemadmin/setup/#openjdk)
- [Elasticsearch](/systemadmin/setup/#elasticsearch)
- [MongoDB](/systemadmin/setup/#mongodb)
- [Tomcat](/systemadmin/setup/#tomcat)
- [Deployment](/systemadmin/setup/#deployment)


https://www.elastic.co/guide/en/elasticsearch/reference/7.2/rpm.html#install-rpm
https://tomcat.apache.org/download-70.cgi
https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-centos-7
https://github.com/CentOS/sig-cloud-instance-images/issues/28

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

Install CentOS 7 onto a VM using the following options:

- Set up correct timezone
- Ensure keyboard is in the correct language (if using english make sure the `~` works)
- Software Selection:
    - Minimal install
        - Compatibility libraries
        - Development Tools
        - Security Tools
        - System Administrator Tools

Once CentOS has finished installing connect the VM to the internet and run `yum update`

__WARING: Some commands may need to be run with sudo.__

#### OpenJDK

1. Install openjdk-8:

    ```sh
    yum install java-1.8.0-openjdk -y
    java -version
    ```

1. Verify that the openjdk version is '1.8.0_***'

#### Elasticsearch

1. Get and install elasticsearch:

    ```sh
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm.sha512
    sha512sum -c elasticsearch-7.2.1-x86_64.rpm.sha512
    rpm --install elasticsearch-7.2.1-x86_64.rpm
    ```

1. Enable elasticsearch as a service and start elasticsearch

    ```sh
    systemctl enable elasticsearch
    service elasticsearch start
    service elasticsearch status
    ```

1. Verify that elasticsearch is running, the last command from above should output something similar to:

    ```ini
    ● elasticsearch.service - Elasticsearch
    Loaded: loaded (/usr/lib/systemd/system/elasticsearch.service; enabled; vendor preset: disabled)
    Active: active (running) since ...
    Docs: http://www.elastic.co
    ...
    ```

#### MongoDB

1. Add the MongoDB yum repository

    ```sh
    sudo vi /etc/yum.repos.d/mongodb.repo
    ```

    Add the following to the file:

    ```ini
    [MongoDB]
    name=MongoDB Repository
    baseurl=http://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.2/x86_64/
    gpgcheck=0
    enabled=1
    ```

1. Install MongoDB

    ```sh
    yum install mongodb-org
    ```

1. Start Mongo and set up as a service

    ```sh
    systemctl enable mongod.service
    systemctl start mongod.service
    ```

1. Verify that MongoDB is installed and started

    ```sh
    mongod --version
    ```

    You should get something like:

    ```ini
    db version v4.2.1
    git version: edf6d45851c0b9ee15548f0f847df141764a317e
    OpenSSL version: OpenSSL 1.0.1e-fips 11 Feb 2013
    allocator: tcmalloc
    modules: none
    build environment:
        distmod: rhel70
        distarch: x86_64
        target_arch: x86_64
    ```

#### Tomcat

1. Install tomcat
```sh
sudo groupadd tomcat
sudo useradd -M -s /bin/nologin -g tomcat -d /opt/tomcat tomcat
sudo mkdir /opt/tomcat
cd ~
wget http://mirror.reverse.net/pub/apache/tomcat/tomcat-7/v7.0.96/bin/apache-tomcat-7.0.96.tar.gz
cd /opt/tomcat
mv apache-tomcat-7.0.96/* .
rm -rf apache-tomcat-7.0.96/
sudo chgrp -R tomcat /opt/tomcat
sudo chmod -R g+r conf
sudo chmod g+r conf
sudo chown -R tomcat webapps/ work/ temp/ logs/
```

```sh
sudo vi /etc/systemd/system/tomcat.service
```

```txt
# Systemd unit file for tomcat
[Unit]
Description=Apache Tomcat
After=elasticsearch.service

[Service]
Type=forking

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/bin/kill -15 $MAINPID

User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```

JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Dapplication.datadir=/var/spoon -Xmx4g -XX:+UseConcMarkSweepGC"




cd /tmp
wget http://us.mirrors.quenda.co/apache/tomcat/tomcat-7/v7.0.96/bin/apache-tomcat-7.0.96.tar.gz
sudo tar -xzf apache-tomcat-7.0.96.tar.gz -C /usr/share/
cd /usr/share/apache-tomcat-7.0.96
sudo groupadd tomcat
sudo mkdir /opt/tomcat
sudo useradd -s /bin/nologin -g tomcat -d /usr/share/apache-tomcat-7.0.96 tomcat

sudo chown -R tomcat:tomcat *
sudo chmod 755 -R *

```sh
sudo vi /etc/systemd/system/tomcat.service
```

```txt
# Systemd unit file for tomcat
[Unit]
Description=Apache Tomcat
After=elasticsearch.service

[Service]
Type=forking

ExecStart=/usr/share/apache-tomcat-7.0.96/bin/startup.sh
ExecStop=/bin/kill -15 $MAINPID

User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```

Add `JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Dapplication.datadir=/var/spoon -Xmx4g -XX:+UseConcMarkSweepGC"` to conf

Get files from 

### Deployment
