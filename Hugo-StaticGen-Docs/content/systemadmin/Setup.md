+++
title = "Setup"
description = "These are instructions on how to get production instance of SPOON set up on a VM."
weight = 2
markup = "mmark"
+++

These are instructions on how to get production instance of SPOON set up on a VM.

<!--more-->

## Walkthrough

This documentation will walk you through setting up a production server to serve SPOON.

- [Prerequisites](/systemadmin/setup/#prerequisites)
- [OpenJDK](/systemadmin/setup/#openjdk)
- [Elasticsearch](/systemadmin/setup/#elasticsearch)
- [MongoDB](/systemadmin/setup/#mongodb)
- [Tomcat](/systemadmin/setup/#tomcat)
- [Deployment](/systemadmin/setup/#deployment)

### Prerequisites

#### Required Hardware (can be run on a VM or dedicated hardware)

- Suggested:

  - CPUs: 4
  - RAM: 8GB
  - DISK: 40GB (Increase, if storing a lot of media and resources locally)

- Minimum:
  - CPUs: 1
  - RAM: 2GB (Application should be set to use 1GB)
  - DISK: 20GB

#### Required Software

- OpenJDK 8
- Elasticsearch 7.2.1
- MongoDB 4.2.0
- Tomcat 7.0.96

### Server Setup

Install CentOS 7 onto a VM using the following options:

- Set up correct time zone
- Ensure keyboard is in the correct language (if using English make sure the `~` works)
- Software Selection:
  - Minimal install
    - Compatibility Libraries
    - Development Tools
    - Security Tools
    - System Administrator Tools

Once CentOS has finished installing connect the VM to the internet and run `yum update`.

**WARING: Some commands may need to be run with sudo.**

#### OpenJDK

1. Install openjdk-8

    ```sh
    yum install java-1.8.0-openjdk -y
    java -version
    ```

1. Verify that the openjdk version is "1.8.0\_\*\*\*"

#### Elasticsearch

1. Get and install Elasticsearch

    ```sh
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.1-x86_64.rpm.sha512
    sha512sum -c elasticsearch-7.2.1-x86_64.rpm.sha512
    rpm --install elasticsearch-7.2.1-x86_64.rpm
    ```

1. Enable Elasticsearch as a service and start Elasticsearch

    ```sh
    systemctl enable elasticsearch
    service elasticsearch start
    service elasticsearch status
    ```

1. Verify that Elasticsearch is running, the last command from above should output something similar to:

    ```sh
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

    Add the following to the file

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

    You should get something like

    ```sh
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

1. Install Tomcat

    ```sh
    yum install tomcat
    ```

1. Update the Tomcat configuration to use a specific data directory

    Paste the following line into `/usr/share/tomcat/conf/tomcat.conf`.

    ```ini
    JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Dapplication.datadir=/var/spoon -Xmx4g -XX:+UseConcMarkSweepGC"
    ```

1. Add an admin user to the Tomcat server (this is only for the tomcat server and not the SPOON application)

    Uncomment out the second-to-last line and change the admin password

    ```sh
    sudo vim /etc/share/tomcat/conf/tomcat-users.xml
    ```

1. Set up Tomcat as a service to start on boot and start Tomcat

    ```sh
    systemctl enable tomcat.service
    systemctl start tomcat.service
    systemctl status tomcat.service
    ```

1. Ensure Tomcat is running

    ```sh
    tomcat version
    ```

    You should get something like:

    ```sh
    Server version: Apache Tomcat/7.0.76
    Server built:   Mar 12 2019 10:11:36 UTC
    Server number:  7.0.76.0
    OS Name:        Linux
    OS Version:     3.10.0-1062.4.1.el7.x86_64
    Architecture:   amd64
    JVM Version:    1.8.0_232-b09
    JVM Vendor:     Oracle Corporation
    ```

### Deployment

1. Copy the war file to the VM (run this command on your local machine)

    ```sh
    scp openstorefront.war username@vm-hostname:~/openstorefront.war
    ```

1. Change the permissions of the war (run this command on the VM)

    ```sh
    chmod 777 ~/openstorefront.war
    chown tomcat:tomcat ~/openstorefront.war
    ```

1. Remove any old SPOON applications

    ```sh
    systemctl stop tomcat
    rm -rf /usr/share/tomcat/webapps/openstorefront /usr/share/tomcat/webapps/openstorefront.war
    systemctl start tomcat
    ```

1. Deploy the new SPOON application

    **If you already have data, see [Add data to the application](#add-data-to-the-application), before doing this step.**

    ```sh
    mv ~/openstorefront.war /usr/share/tomcat/webapps/
    ```

1. Navigate to `localhost:8080/openstorefront/` to view SPOON

1. If something has gone wrong and you are unable to view SPOON, use journalctl to debug the issue

    ```sh
    journalctl -u tomcat -f
    ```

#### Add data to the application

The application will create some base data for you, but if you have data from a previous instance, you can use it with the application using these steps.

1. Create the data directory (SPOON will automatically create data if data is not found)

    ```sh
    mkdir /var/spoon
    ```

1. Copy the data from your local machine to the VM (should be run on your local machine)

    ```sh
    scp spoon-data.tar.gz username@VM-address:~/spoon-data.tar.gz
    ```

1. Unpack the data and change permissions to allow Tomcat to access it (should be run on the VM)

    ```sh
    tar -zxf ~/spoon-data.tar.gz -C /var/spoon
    mv /var/spoon/spoon/* /var/spoon
    rm -rf /var/spoon/spoon
    chown -R root:tomcat /var/spoon
    chmod 777 -R /var/spoon
    ```
