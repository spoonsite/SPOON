+++
title = "Vendor Interaction Guide"
description = ""
weight = 15
+++


This guide goes over various issues developers would be concerned with, such as how to setup their IDE, what libraries are used, etc.
<!--more-->

# 1. Internal Development

See [Server Code Standard](/dev/server-code-standard), and [Front-end Code Standards](/dev/front-end-code-standard) for Coding and Style guides.


## 1.1 Dev Environment Set up with NetBeans

1. Install openjdk 8
    - Windows: https://github.com/ojdkbuild/ojdkbuild
    - Linux: ```sudo apt install openjdk-8-jdk```

1. Install netbeans 8.2 Java EE version
    - https://netbeans.org/downloads/8.2/
    - Run the installer, and during install don't checkbox either server box (glassfish or tomcat)
        * Point netbeans to the openjdk 8 installation path
            * Windows: "C:\Program Files\ojdkbuild\java-1.8.0-openjdk-1.8.0.222-2"
            * Linux: /usr/lib/jvm/java-8-openjdk-amd64/

1. Download tomcat 7.0.96 binaries from https://tomcat.apache.org/download-70.cgi
    - Unpack tomcat

1. Download elasticsearch 7.2.1 from https://www.elastic.co/downloads/past-releases/elasticsearch-7-2-1
    - Once it is downloaded you need to unpack elasticsearch
    - Inside the bin folder of elasticsearch you need to find and edit the elasticsearch file. 
        * It is a .bat file for Windows
        * For Linux is is a shell file
    - At the top of this file, you need to add the following line
        * Windows: set JAVA_HOME=C:\Program Files\ojdkbuild\java-1.8.0-openjdk-1.8.0.222-2
        * Linux: JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
    - Now start up elasticsearch by double clicking on the file that was just modified.

1. Download the latest maven 3.6 binaries from https://maven.apache.org/download.cgi
    - Unpack maven

1. Clone the project from where ever it is located
    - clone project
    - For example `git clone https://github.com/spoonsite/SPOON.git`

1. Start netbeans
    - netbeans > tools > options > java > maven > maven Home > "Point to maven folder downloaded from above" > apply > OK
    - netbeans > services > servers > add server > apache tomcat or tomEE > next > "Point to downloaded tomcat" > " Set user name and password = 1" > finish
    - netbeans > services > "just created server" > properties > Platform > VM Options > "-Xms8G -Dapplication.datadir=E:/SPOON_PROJECT_DATA"
    - netbeans > File > open project > "cloned project" > Open Project > spoon_Directory=>server_Directory=>openstorefront > Open Required Projects > Open Project
    - Resolve Problems > "Resolve each problem until all green." >> Close
  
1. Back in netbeans we need to clean the top level openstorefront project in the projects tab,(right click) then build with dependencies.(right-click)
    - Once that is completed(it can take several minutes the first time.)
    - run the openstorefront-web project

1. Start up a web browser, navigate to localhost:8080/openstorefront
    - login as an admin
    - Go to local Admin -> Admin Tools
    - Application Management -> System
    - Search Control -> Re-Index Listings
    - You may have to run the re-indexer twice.

1. Finally we can start up a dev server for developing front end stuff.
    - In a terminal navigate to the spoon project and step into the client=>desktop folder
    - Run: ```npm install```
    - Then run: ```npm run serve```


## 1.2 Dev Environment with DOCKER
    COMING SOON!