+++
title = "Developer Setup Guide"
description = "Developer Setup Guide"
weight = 1
+++

This guide describes various issues developers would be concerned with, such as how to set up their IDE (integrated development environment), what libraries are used, etc.
<!--more-->

See [Server Code Standard](/dev/server-code-standard), and [Front-end Code Standards](/dev/front-end-code-standard) for Coding and Style guides.

## Dev Environment Set up with NetBeans

1. Install openjdk 8
    - Windows: [https://github.com/ojdkbuild/ojdkbuild](https://github.com/ojdkbuild/ojdkbuild)
    - Linux: `sudo apt install openjdk-8-jdk`

1. Install the latest Netbeans version from [https://netbeans.apache.org/download/](https://netbeans.apache.org/download/)
    - Run the installer, and during install don't check either server box (glassfish or tomcat)
    - Point Netbeans to the Openjdk 8 installation path
        - Windows: `C:\Program Files\ojdkbuild\java-1.8.0-openjdk-1.8.0.252-2`
        - Linux: `/usr/lib/jvm/java-8-openjdk-amd64/`

1. Download Tomcat 7 binaries from [https://tomcat.apache.org/download-70.cgi](https://tomcat.apache.org/download-70.cgi)
    - Unpack Tomcat

1. Download Elasticsearch 7.2.1 from [https://www.elastic.co/downloads/past-releases/elasticsearch-7-2-1](https://www.elastic.co/downloads/past-releases/elasticsearch-7-2-1)
    - Once it is downloaded, unpack Elasticsearch
    - Once unpacked, find the executable for elasticsearch in `elasticsearch/bin/`
        - It is a .bat file for Windows
        - For Linux is is a shell file
    - Set the `JAVA_HOME` environment variable at the top of the elasticsearch executable
        - Windows: `set JAVA_HOME=C:\Program Files\ojdkbuild\java-1.8.0-openjdk-1.8.0.252-2`
        - Linux: `JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64`
    - Start Elasticsearch by double-clicking the file that was just modified

1. Download the latest Maven 3.6 binaries from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
    - Unpack Maven

1. Clone the SPOON project
    - If you are using git: `git clone https://github.com/spoonsite/SPOON.git`

1. Start and configure Netbeans
    - Netbeans > tools > options > Java > Maven > Maven Home > "Point to maven folder downloaded from above or use bundled if version 3.6" > Apply > OK
    - Netbeans > tools > options > Java > Maven > Dependencies > select "Every Project Open" for each dropdown > Apply > OK
    - Netbeans > tools > servers > add server > Apache Tomcat or TomEE > next > "Point to downloaded tomcat" > "Set user name and password" > finish
    - Netbeans > tools > "just created server" > Platform > VM Options > `-Xms8G -Dapplication.datadir=/empty/directory/for/data`
    - Netbeans > File > open project > "cloned project" > Open Project > highlight `spoon_Directory/server/openstorefront` > select "Open Required Projects" > Open Project
    - Resolve Problems > "Resolve each problem until all green." > Close

1. Clean the project in Netbeans
    - Clean the top-level openstorefront project (right-click and select clean)
    - Build with dependencies (right-click and select build with dependencies, NOTE: this may take a long time as it has to download many packages)

1. Run the project
    - Start mongo server (See MongoSetupGuide for help with MongoDB setup)
    - Start Elasticsearch
    - Run the openstorefront-web project in NetBeans (right-click and select run)

1. Open a web browser to `localhost:8080/openstorefront`
    - Login as an admin
    - Go to local Admin -> Admin Tools
    - Application Management -> System
    - Search Control -> Re-Index Listings (this may take a while and may fail the first time, if it does, just run the indexer again)

1. Start the dev server for Vue.js for developing front-end code
    - In a terminal navigate to the spoon project and step into the client=>desktop folder
    - Run: `npm install`
    - Then run: `npm run serve` (openstorefront-login uses the command `npm run dev`)
