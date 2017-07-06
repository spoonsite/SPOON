
# Setup Guide

# 1.  Installation
 -----

## 1.1  High level instructions for a fresh install
-------------------------------------------

Prior to the install, setup an Elasticsearch *OR* Solr instance and make sure it's
running. Then, perform the following steps:

1.  Setup VM

2.  Install Java JDK 1.8

3.  Install Tomcat 7

4.  Integrate OpenAM Agent

5.  Deploy Application

6.  Configure Application

7.  Restart Tomcat (To pick up configuration changes)

8.  Import data

## 1.2  Suggested VM Configuration
--------------------------

The following is the recommended VM configuration:

-   CentOS

-   CPUs: 4

-   RAM: 8GB

-   DISK: 40GB (Increase, if storing a lot of media and resources locally)

-   Minimum:

    -   CPUs: 1

    -   RAM: 2GB (Application should be set to use 1GB)

    -   DISK: 20GB

## 1.3  Platform Dependencies
---------------------

The Storefront is dependent upon:

-   Java 8

-   Tomcat 7 v50+

## 1.4  External Dependencies
---------------------

The Storefront relies upon the following external dependencies:

-   OpenAM (Optional)

-   Index Search Server (Pick one)
	
	-   Solr 6.x + *Recommended for greater control*

	-   **Elasticsearch 2.3.x *Recommended for simple install*  Recommended search server**
	
*Support for ESA 1.0 and Solr 4.3.1 has been dropped*


### 1.4.1 To Use Solr (Optional)

(The following assumes the application is stop prior to the change. To change on a running application you need to restart the SearchServerManager after making changes.)

Download Version 6.x 
from [solr home](http://lucene.apache.org/solr/), and then perform the
following steps:

1. Unpackage  (setup according to solr instructions if setting up OS integration)

2. Add core
-copy server /doc/solr/openstorefront (From source code) to .../solr-6.1.0/server/solr

3.  Start server
bin/solr start -p 8983 

http://localhost:8983 should bring up the admin consol if it's working.

4.  Configure OpenStorefront to point to Solr by going to:
    /var/openstorefront/config/openstorefront.properties

5.  Edit 
	
	search.server=solr

	solr.server.url=http://localhost:8983/solr/openstorefront

	solr.server.usexml=true

6. Resync data 

	a) Nav->Admin->System->Search Control

        b) Click Re-Index Listings

### 1.4.1.1 Installing as service linux
run as sudo 

1) ln -s /usr/local/solr/solr-6.1.0 latest

2) cp /usr/local/solr/solr-6.1.0/bin/init.d/solr /etc/init.d

3) nano /etc/init.d/solr

Edit:

> SOLR_INSTALL_DIR="/usr/local/solr/latest"

> SOLR_ENV="/usr/local/solr/latest/bin/solr.in.sh"
 
> SOLR_INCLUDE="$SOLR_ENV" "$SOLR_INSTALL_DIR/bin/solr" "$SOLR_CMD" "$2"

Save and exit

4)  Add User
> chmod 755 /etc/init.d/solr

> chown root:root /etc/init.d/solr

(debian/ubuntu)

> update-rc.d solr defaults

> update-rc.d solr enable

(centos/redhat) 

> chkconfig solr on

> groupadd solr

> useradd -g solr solr

> chown -R solr:solr /usr/local/solr/solr-6.1.0

> chown solr:solr latest

5) (If lsof is not installed)
> yum install lsof

6) (This will start at port 8983)
> service solr start|stop  


### 1.4.2 To Use Elasticsearch (Optional)

1. Download
	[elasticsearch home]https://www.elastic.co/ (Apache v2 licensed)

2. Start
	<elasticsearch>/bin/elasticsearch
	
http://localhost:9200 should return some json with stats.

3. Configure OpenStorefront to point to Solr by going to: /var/openstorefront/config/openstorefront.properties or System admin screen

		Add/Set: (adjust as needed to match url and ports)
	
		search.server=elasticsearch

		elastic.server.host=localhost

		elastic.server.port=9300

4. Resync data 

	a) Nav->Admin->System->Search Control

        b) Click Re-Index Listings
  
### 1.4.2.1 Yum install of Elasticsearch 

1. Download and install with YUM 
https://www.elastic.co/downloads/elasticsearch (2.x) 
(see https://www.elastic.co/guide/en/elasticsearch/reference/2.3/setup-repositories.html for yum install instructions) 

2. service elasticsearch start 

http://localhost:9200 should return some json with stats.

3. Configure OpenStorefront to point to elastisearch by going to: /var/openstorefront/config/openstorefront.properties or System admin screen->system properties 

4. Add/Set: (adjust as needed to match url and ports) 

search.server=elasticsearch 
elastic.server.host=localhost 
elastic.server.port=9300 

5. Resync data 

    a) Nav->Admin->Application Data->System->Search Control 
    b) Click Re-Index Listings        


### 1.4.3 Updated Search Server at Runtime

1. Use Admin->Application Management->System to set the system config properties 

2. On Managers tab -> Restart Search Server Managers



## 1.5  System Setup
------------

Unless otherwise noted, run as sudo.

**NOTE:** You can use Nano or another text editor.

###1.5.1 Install Java

Use the following steps to install Java.

1.  Download JDK from Oracle

2.  Move the downloads to the server home directory

3.  Run Sudo su as root

4.  Extract and move following folders:

    -   mkdir /usr/java

    -   tar -xvf jdk-8u25-linux-x64.tar.gz

    -   mv jdk1.8.0\_25 /usr/java/

    -   chmod 755 -R /usr/java

5.  Create the following links:

    -   ln -s /usr/java/jdk1.8.0\_25 /usr/java/jdk8

    -   ln -s /usr/java/jdk1.8.0\_25 /usr/java/latest

6.  Setup Environment Vars using the instructions at
    http://www.cyberciti.biz/faq/linux-unix-set-java\_home-path-variable/,
    and then perform the following:

	a.  nano /etc/profile

	b.  Add to the bottom:

		-   #Java Path

		-   export PATH=\$PATH:/usr/java/latest/bin

		-   export JAVA\_HOME=/usr/java/latest

	c.  Save/Exit then source /etc/profile

	d.  nano /etc/bash.bashrc

	e.  Add to the bottom:

		-   #Java Path

		-   export PATH=\$PATH:/usr/java/latest/bin

		-   export JAVA\_HOME=/usr/java/latest

7.  Save/Exit then source /etc/bash.bashrc

8.  Confirm that java -version runs

### 1.5.2 Install Tomcat Public Package
------

Use the following steps to install the Tomcat public package.

1.  Download and copy to home directory

2.  tar -xvf apache-tomcat-7.0.55.tar.gz

3.  mv apache-tomcat-7.0.55 /usr/local/tomcat

4.  ln -s /usr/local/tomcat/apache-tomcat-7.0.55
    /usr/local/tomcat/latest
	(Use ln -nsf (target) (link) to repoint)

5. nano /usr/local/tomcat/latest/bin/setenv.sh

> Add line: CATALINA\_OPTS=-Xmx1024m

**NOTE:** Memory settings depend on server config. Minimum recommended
memory is 1GB; however, it may be able to run on less. The amount of
memory affects the amount of concurrent users the server can support.

> VM RAM:
>
> 2GB -&gt; -Xmx1024m
>
> 4GB -&gt; -Xmx2048m
>
> 8GB -&gt; -Xmx6144m (Make sure to use a 64bit VM)

**OPTIONAL:** In the conf/server.xml (In Connector section)

-   Add http compression on. 
    Compression="on"
    compressableMimeType="text/html,text/xml,text/plain,application/json,text/css,application/javascript"

- Set to use NIO Protocal
  protocol="org.apache.coyote.http11.Http11NioProtocol"
  maxThreads="1000"

> Also, you may consider setting more aggressive connection settings
> than the default depending on your expected load. See Tomcat
> documentation: <http://tomcat.apache.org/tomcat-7.0-doc/index.html>.
> The default settings will suffice for most deployments.

>6\. Setup Tomcat as a service using the following:

[See Installing tomcat 7 on CentOS](http://www.davidghedini.com/pg/entry/install\_tomcat\_7\_on\_centos)

>a.  nano /etc/init.d/tomcat

> \#!/bin/bash
>
> \# description: Tomcat Start Stop Restart
>
> \# processname: tomcat
>
> \# chkconfig: 234 20 80
>
> \#JAVA\_HOME=/usr/java/jdk8
>
> \#export JAVA\_HOME
>
> \#PATH=\$JAVA\_HOME/bin:\$PATH
>
> \#export PATH
>
> CATALINA\_HOME=/usr/local/tomcat/latest
>
> case \$1 in
>
> start)
>
> sh \$CATALINA\_HOME/bin/startup.sh
>
> ;;
>
> stop)
>
> sh \$CATALINA\_HOME/bin/shutdown.sh
>
> ;;
>
> restart)
>
> sh \$CATALINA\_HOME/bin/shutdown.sh
>
> sh \$CATALINA\_HOME/bin/startup.sh
>
> ;;
>
> esac
>
> exit 0

>b.  chmod 755 /etc/init.d/tomcat

>c.  chkconfig --add tomcat

>d.  chkconfig --level 234 tomcat on

>7\. Open port iptables -I INPUT -p tcp --dport 8080 -j ACCEPT

### 1.5.3 Install Tomcat Using JPackage
-----

Use the following steps to install Tomcat using JPackage
[See jpackage.org](http://www.jpackage.org/)

1.  At www.jpackage.org, follow the installation instructions to setup
    the repository for your system install (e.g. yum, apt, and urpmi)

2.  After you have set up the repository, pull down Tomcat7-7.0.54-2
    from the version 6 repo.

**NOTE:** JPackage version has a mistake in the package. The ecj jar
library is old and doesn't work with JDK1.8 ; however, this is easily
corrected. Download 7.0.54 from apache.org and replace ecj-xxx.jar in
tomcat/lib directory with ecj-xxx.jar from the 7.0.54 version.

### 1.5.4 Server Control
----

Use the following commands to control the server.

-   service tomcat start

-   service tomcat stop

> **NOTE:** It can take a minute for the application to shut down.
> Please wait before restarting.

-   service tomcat restart

> **NOTE:** Using this is not recommended as it not always successful
> due the script not waiting for shutdown.

## 1.6 Deploying application
---------------------

To deploy the application, copy openstorefront.war to
/usr/local/tomcat/latest/webapps

## 1.7 Application Configuration
-------------------------

The application configuration and data are stored in
/var/openstorefront/. Make sure the user running the application has r/w
permission for that directory.  All directories are created upon
application startup. The high level directory map is stored under
/var/openstorefront/.

-   config - holds all configurations files. Defaults are created on
    initial startup.

-   db - holds database files

> **NOTE:** When upgrading to version 2.3+ from 2.2 and earlier, there is
> an additional step when existing data needs to persist. Navigate
> to the database directory: /var/openstorefront/db/databases/openstorefront
> and delete the openstorefront .wal file (openstorefront*.wal).
> First creating a complete backup of the database files would be prudent.
> Deleting this file allows the database to be upgraded to a newer version
> which is used starting with version 2.3.

-   import -directory for placing files to be imported.

-   perm - permanent storage location

-   temp - temporary storage that application controls.

NOTE: most application temp storage defaults to the system temp storage
location. The temp directory here holds information that needs to be
persisted for a longer time period. The main configuration file is:
/var/openstorefront/config/openstorefront.properties

On initial setup modify the following:

1.  change the url to point to esa/solr
    "solr.server.url=http://localhost:15000/solr/esa"

2.  For OpenAM Integration:

a.  \#Security Header

> openam.url=&lt;Url to open am something like
> http:/idam.server.com/openam
>
> \#http:/.../openam/UI/Logout (Full URL)
>
> logout.url=&lt;Full url to the logout&gt;
>
> \#Change any header as needed. The defaults likely will work out of
> the box.
>
> openam.header.username=sAMAccountName
>
> openam.header.firstname=givenname
>
> openam.header.lastname=sn
>
> openam.header.email=mail
>
> openam.header.phone=telephonenumber
>
> openam.header.group=memberOf
>
> openam.header.ldapguid=memberid
>
> openam.header.organization=
>
> openam.header.admingroupname=STORE-Admin

b.  Edit shiro.ini under the config directory

> under \[main\]
>
>> uncomment (remove \#)
>
>> headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm
>
>> securityManager.realms = \$headerRealm
>
> under \[users\]
>
>> comment out
>
>> \#admin = secret, administrator
>
>> \#user = user
>
> under \[roles\]
>
>> comment out
>
>> \#admin = administrator

### 1.7.1 Open AM Notes:
-------------
If when setting Open AM up with certificates you may need to use a truststore on openstorefront tomcat.  If so, remember to update the certificates in the truststore when the certificate changes.  Open AM, Openstorefront, and proxy if used will all need to have valid matching certificates.


## 1.8 Importing Data
--------------

Import data using the following steps.

1.  Export (or import) data from an existing system using application
    > admin tools. (Requires an Admin Login)

2.  When the application is first started, it will load a default set of
    "lookup" types. These can be later changed using the admin tools. On
    a new install, no attributes, articles or components are loaded.
    They can be entered or imported using the application admin tools.

## 1.9  Logging Notes
-------------

You can view the logs messages in the Catalina log file:
/usr/local/tomcat/latest/logs

Also, an admin can turn on DB logging from the System admin screen by default DB logging is off.  It’s on recommended to turn on DB logging for specific troubleshooting and turn off when done.

### 1.9.1 Log Level Definitions
-------

See the following table for the log definitions.

-  **SEVERE**    -  Something didn't run correctly or as expected
-  **WARN**      -  Behavior may not be desired but, the system was able to continue
-  **INFO**      -  System admin message
-  **FINE**      -  Developer message
-  **FINER**     -  Detailed developer messages
-  **FINEST**    -  Trace information

### 1.10 Setup OpenAM
------------

See the example below for OpenAM setup. Your configuration may be
different.

(See https://bugster.forgerock.org/jira/browse/OPENAM-211: J2EE agents
are unable and will not work, if the container was started prior to OpenAM).

**NOTE:** When the OpenAM policy agent is installed on Tomcat, the
application server will not start unless the OpenAM server is available.
This is a known issue with the OpenAM Policy agent.

### 1.10.1 Versions Used
------

The following versions were used:

-   [Tomcat 7.0.55 64-bit Windows
    zip](http://mirrors.sonic.net/apache/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55-windows-x64.zip)

-   [OpenAM
    11.0.0.0](https://backstage.forgerock.com/downloads/enterprise/openam/openam11/11.0.0/OpenAM-11.0.0.zip):

    -   <http://docs.forgerock.org/en/openam/11.0.0/release-notes/index/index.html>

-   [J2EE Policy Agent 3.3.0 Apache Tomcat 6 and
    7](https://backstage.forgerock.com/downloads/enterprise/openam/j2eeagents/stable/3.3.0/Tomcat-v6-7-Agent-3.3.0.zip):

    -   <http://docs.forgerock.org/en/openam-pa/3.3.0/jee-release-notes/index/index.html>

-   64 bit JRE 1.7.0\_67-b01

### 1.10.2 Installation of OpenAM Java EE Policy Agent into Tomcat 7.0.55
--------

Use the following steps to install OpenAM Java EE Policy Agent on
Tomcat.

1.  Make sure the Agent Profile has already been created in OpenAM

2.  Create a *pwd.txt* file at C:\\Temp\\pwd.txt and add your Agent
    Profile password to it

3.  Shutdown the Tomcat server that is going to run your web application

4.  Make sure the Tomcat server that is running OpenAM is still running

5.  Extract Tomcat-v6-7-Agent-3.3.0.zip to a known directory

6.  CD into the j2ee\_agents/tomcat\_v6\_agent/bin directory

7.  Execute agentadmin --install to install the agent

### 1.10.3 References
------

-   <http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/index/chap-apache-tomcat.html>

-   <http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/#chap-apache-tomcat>

### 1.10.4 Configuration of OpenAM
------

See
[Open AM Getting Started](http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/getting-started/)
for OpenAM configuration information.

### 1.10.5 Configure the Policy in OpenAM
-------

Use the following steps to configure the OpenAM policy.

1.  Open up OpenAM in a web
    browser http://(host):8080/openam

2.  Log into OpenAM using amadmin

3.  Click on Access Control -> /(Top Level Realm) -> Policies

4.  Click on New Policy

>>a.  Give the Policy a name of Storefront Policy

>>b.  In the Rules table click New

>>>i.  Select URL Policy Agent and click **Next**

>>>ii. Enter the following in Step 2 of 2: New Rule

>>>-   Name: Allow Storefront Access

>>>-   Resource Name: http://(host):8081/agentsample/

>>>-   Check the boxes for GET and POST

>>c.  In the Subjects table click **New**

>>>i.  Select Authenticated Users and click Next

>>>ii. Name the rule All Authenticated Users

>>>iii. Click **Finish**

>>d.  Create a new response provider

>>-   In the Dynamic Attribute make sure uid and isMemberOf is
    selected (ctrl-click)

>>-   Click on **Finish**

### 1.10.6 Creating the Agent Profile
-----

Use the following steps to create the agent profile.

1.  Open up OpenAM in a web
    browser http://(host):8080/openam

2.  Log into OpenAM using amadmin

3.  Click on **Access Control** &gt; **Top Level Realm** &gt;
    **Agents** &gt; **J2EE**

4.  Create a new J2EE agent by clicking on the **New...** button under
    Agent

5.  Create the agent with the following parameters:

>-   Name: myagent

>-   Password: password

>-   Configuration: Centralized

>-   Server URL: http://(host):8080/openam

>-   Agent URL: http://(host):8081/agentsample

# 2 CentOS 7 Install with Apache proxy Example
----

Run the following as sudo:

### Optionally install nano:
1)	yum install nano

### Install Java 1.8:
1)	yum install java-1.8.0-openjdk

### Install apache 2.4:
1)	yum clean all
2)	yum -y update
3)	yum -y install httpd
4)	firewall-cmd --permanent --add-port=80/tcp
5)	firewall-cmd --permanent --add-port=443/tcp
6)	firewall-cmd --reload

    NOTE)   If firewall-cmd doesn't exist then run 

    a) yum install firewalld -y

    b) systemctl start firewalld

    c) systemctl enable firewalld

7)	systemctl start httpd
8)	systemctl enable httpd

apache service command reference (apache should be running at this point):

**Start:** systemctl start httpd

**Stop:** systemctl stop httpd

**Test:** curl localhost | grep 'Apache HTTP server'

should give you a response something like this:
```
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  4897  100  4897    0     0   928k      0 --:--:-- --:--:-- --:--:--  956k
                <p class="lead">This page is used to test the proper operation of the <a href="http://apache.org">Apache HTTP server</a> after it has been installed. If you can read this page it means that this site is working properly. This server is powered by <a href="http://centos.org">CentOS</a>.</p>
```

### Install elasticsearch 2.x:
1)	rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch
2)	Add the following in your /etc/yum.repos.d/ directory in a file with a .repo suffix, 
for example /etc/yum.repos.d/elasticsearch.repo

```
[elasticsearch-2.x]
name=Elasticsearch repository for 2.x packages
baseurl=https://packages.elastic.co/elasticsearch/2.x/centos
gpgcheck=1
gpgkey=https://packages.elastic.co/GPG-KEY-elasticsearch
enabled=1
```
3)	yum install elasticsearch
4)	systemctl daemon-reload
5)	systemctl enable elasticsearch.service
6)	systemctl start elasticsearch.service

elasticsearch service command reference (elasticsearch should be running at this point):

**Start:** systemctl start elasticsearch.service

**Stop:**  systemctl stop elasticsearch.service

**Test:** curl -XGET 'localhost:9200/?pretty'

should give you a response something like this:
```
{
  "name" : "Cp8oag6",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "AT69_T_DTp-1qgIJlatQqA",
  "version" : {
    "number" : "5.4.3",
    "build_hash" : "f27399d",
    "build_date" : "2016-03-30T09:51:41.449Z",
    "build_snapshot" : false,
    "lucene_version" : "6.5.0"
  },
  "tagline" : "You Know, for Search"
}
```

### Install tomcat 7:
(https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-7-on-centos-7-via-yum)
1)	yum install tomcat
2)	yum install tomcat-webapps tomcat-admin-webapps 
3)	systemctl enable tomcat
4)	systemctl start tomcat

tomcat service command reference (tomcat should be running at this point):

**Start:** systemctl start tomcat

**Stop:**  systemctl stop tomcat

**Install dir:**  /usr/share/tomcat/

**Test:** curl localhost:8080 | grep 'successfully installed Tomcat'

should give you a response something like this:
```
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 11197    0 11197    0     0   850k      0 --:--:-- --:--:-- --:--:--  911k
                    <h2>If you're seeing this, you've successfully installed Tomcat. Congratulations!</h2>
```

### Configure Apache proxy:
(https://www.digitalocean.com/community/tutorials/how-to-use-apache-as-a-reverse-proxy-with-mod_proxy-on-centos-7)
1)	Install mod-proxy : already installed	
2)	Install web socket support: already installed
3)	nano /etc/httpd/conf.d/default-site.conf

**Add:**

```
<VirtualHost *:80>
   ProxyPreserveHost On	    
   RewriteEngine on

   # Openstorefront reverse proxy
   ProxyPass "/openstorefront/" "http://localhost:8080/openstorefront/"
   ProxyPassReverse "/openstorefront/" "http://localhost:8080/openstorefront/"
   
   # Openstorefront web socket reverse proxy
   ProxyPass "/openstorefront/" "ws://localhost:8080/openstorefront/"
   ProxyPassReverse "/openstorefront/" ws://localhost:8080/openstorefront/"
   
   # changes <hostname>/ to <hostname>/openstorefront/
   RedirectMatch ^/$ /openstorefront/
</VirtualHost>
```
4)	If you have SELinux running (which is installed by default on CentOS) then we need the instructions from here (http://sysadminsjourney.com/content/2010/02/01/apache-modproxy-error-13permission-denied-error-rhel/)

    a)	/usr/sbin/setsebool -P httpd_can_network_connect 1
    
5) 	systemctl restart httpd

### Configure Tomcat:

**Set memory**

1)	nano /usr/share/tomcat/conf/tomcat.conf
Add the following to the existing file
```
JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Xmx4g -XX:+UseConcMarkSweepGC"
```

**Update networking**

2)	nano /usr/share/tomcat/conf/server.xml
		edit: <Connector port="8080"…     to 
```	
	<Connector port="8080" protocol="org.apache.coyote.http11.Http11NioProtocol"
               connectionTimeout="20000"
               maxThreads="250" minSpareThreads="25"
               compression="on" compressableMimeType="text/html,text/xml,text/plain,application/json,text/css,application/javascript"
               enableLookups="false" disableUploadTimeout="true"
               acceptCount="100"
               redirectPort="8443" />
```

3)	systemctl restart tomcat

## Deploy code

1)	mkdir /var/openstorefront
2)	chown -R tomcat:tomcat /var/openstorefront
3)	Download storefront webapp from ```https://github.com/di2e/openstorefront/releases/download/<version tag>/openstorefront.war```
	
    example: ```curl -LO https://github.com/di2e/openstorefront/releases/download/v2.3/openstorefront.war```
4)	chown tomcat:tomcat openstorefront.war
5)	mv openstorefront.war /usr/share/tomcat/webapps 
6) 	systemctl restart tomcat
