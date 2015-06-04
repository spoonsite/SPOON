
#Clearinghouse Administrator Guide

Version 1.4


Space Dynamics Laboratory

Utah State University Research Foundation

1695 North Research Park Way

North Logan, Utah 84341

![](media/image1.png)


#Overview
-----

The Open Storefront application is a catalog of software components that
are of interest to the DI2E community. Components include Government off
the shelf (GOTS), commercial off the shelf (COTS), and Open Source
software (OSS). The component evaluations done by DI2E’s Centers of
Excellence are displayed in the Storefront and give details on the
following:

-   Ownership

-   Where/How to access the software

-   Software vitals

-   Conformance

-   Links to documentation, source code and other artifacts

-   Evaluation information

The application can also be used as general cataloging system.

Open Storefront is developed by Space Dynamics Laboratory and is
licensed under Apache V2.

1.  Client Architecture
    ===================

    1.  <span id="_Toc398129580" class="anchor"><span id="_Toc398195575" class="anchor"><span id="_Toc419188292" class="anchor"></span></span></span>Client Architecture Diagram
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

![](media/image3.png)

Figure . Client Architecture Diagram

<span id="_Toc398129581" class="anchor"><span id="_Toc398195576" class="anchor"><span id="_Toc419188293" class="anchor"></span></span></span>Client Details
-----------------------------------------------------------------------------------------------------------------------------------------------------------

The client core structure is based on Angular.js. The UI core elements
are based on Bootstrap, Angular-strap, and JQuery UI components.
Component definitions are as shown below:

-   **Views:** Display the output. These consist of html template
    snippets with an Angular.js directive that drives the display.

-   **Directives:** Allow for building reusable components for
    the Views.

-   **Controllers:** Each view and some directives have a
    backing controller. The controller function is used to handle the
    interaction between the View and Services. This where the event
    handling occurs and the data scope binding happens.

-   **Services:** Handle the interaction with the server and any
    business logic associated with the data.

    1.  <span id="_Toc398129582" class="anchor"><span id="_Toc398195577" class="anchor"><span id="_Toc419188294" class="anchor"></span></span></span>Client Build Platforms/Tools
        -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The client build environment relies on the following platforms/tools:

  **Platform/Tool**   **Description**
  ------------------- ------------------------------------------------------
  Node.js             Provide the runtime environment for the build tools
  Ruby/Compass        Used for dynamic css generation
  Bower               Handles the third-party web components
  Yeoman              Used to generate the structure of the code artifacts
  Grunt               Builds the code

1.  <span id="_Toc398129583" class="anchor"><span id="_Toc398195578" class="anchor"><span id="_Toc419188295" class="anchor"></span></span></span>Server Architecture
    ================================================================================================================================================================

    1.  <span id="_Toc398129584" class="anchor"><span id="_Toc398195579" class="anchor"><span id="_Toc419188296" class="anchor"></span></span></span>Server Architecture Diagram
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The server architecture is shown in Figure 2.

![](media/image4.png)

Figure . Server Architecture Diagram

<span id="_Toc398129585" class="anchor"><span id="_Toc398195580" class="anchor"><span id="_Toc419188297" class="anchor"></span></span></span>Server Details
-----------------------------------------------------------------------------------------------------------------------------------------------------------

Component definitions are as shown below:

  **Component**        **Definition**
  -------------------- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  **Security**         Authentication and authorization is delegated to OpenAm. This is configured through a custom realm using the Apache Shiro library. All request are passed through this filter.
  **REST API**         The REST API is the component that handles the data interaction between the clients and provides the interface with which the clients can communicate. The REST API is broken into two sections: resources and services. Resources handle the CRUD operations on the data. Service handle operation across data sets. This provides a clean and clear API for integrators.
  **API Docs**         The API docs are generated live based on the currently running code. This keeps the documents always current and reduces maintenance. Other system related call backs (e.g., retrieving binary resources, login handling, etc.) are handled through the Stripes framework.
  **Business Layer**   **Managers**
                       **Services**
                       **Models**
                       **Import/ Export**

<span id="_Toc398129586" class="anchor"><span id="_Toc398195581" class="anchor"><span id="_Toc419188298" class="anchor"></span></span></span>Server Build Platforms/Tools
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The client build environment relies on the following platforms/tools:

  **Platform/Tool**   **Description**
  ------------------- --------------------------------------------------------------------
  Java                Core language and platform
  Maven               Used for the project structure, building and dependency management

1.  <span id="_Toc398129587" class="anchor"><span id="_Toc398195582" class="anchor"><span id="_Toc419188299" class="anchor"></span></span></span>Runtime Environment
    ================================================================================================================================================================

    1.  <span id="_Toc398129588" class="anchor"><span id="_Toc398195583" class="anchor"><span id="_Toc419188300" class="anchor"></span></span></span>Runtime Environment Diagram
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The runtime environment is shown in Figure 3.

![](media/image5.png)

Figure . Runtime Environment diagram

<span id="_Toc398129589" class="anchor"><span id="_Toc398195584" class="anchor"><span id="_Toc419188301" class="anchor"></span></span></span>Runtime Details
------------------------------------------------------------------------------------------------------------------------------------------------------------

The runtime environment relies upon the following applications:

  **Application**   **Description**
  ----------------- -----------------------------------------------------------------------------------
  Proxy Server      This is an external system that proxies requests to the application server.
  Tomcat 7          Tomcat is the web container used to host the storefront application.
  Java 8            It the runtime platform which runs Tomcat
  OS/VM             Is the host machines operating system
  Solr              Enterprise search appliance run externally
  OpenAM            OpenAM runs externally and a policy agent in Tomcat make sure the site is secure.

<span id="_Toc398129590" class="anchor"><span id="_Toc398195585" class="anchor"><span id="_Toc419188302" class="anchor"></span></span></span>**Runtime Component Integration Vectors**
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

<span id="_Toc398113412" class="anchor"><span id="_Toc398113589"
class="anchor"><span id="_Toc389823604"
class="anchor"></span></span></span>

The runtime component integration vectors are shown in Figure 4.

![](media/image6.png)

Figure . Runtime Component Integration Vectors

<span id="_Toc398129592" class="anchor"><span id="_Toc398195586" class="anchor"><span id="_Toc419188303" class="anchor"></span></span></span>Component Integration Vectors Details
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The component integration vectors (CIV) are show in the table below.

  **Source Component**     **Class**   **Target Component**     **Notes**
  ------------------------ ----------- ------------------------ ---------------------------------------------------------------------------------------------------
  openstorefront           C           Solr/ESA                 
  openstorefront           C           OpenAM                   OpenAM with their policy agent; requires a hard tie to the application and the application server
  openstorefront           B           JEE Application Server   Currently configured to deploy on Tomcat
  Orient DB                B           openstorefront           Embedded
  JEE Application Server   A           OS/VM                    Currently targeted for CentOS

The CIVs represent an integration activity involving a source, Component
A, and a target, Component B.

The CIVs, as defined by the DI2E PMO, are as follows:

-   **Class A: A-deployed On-B**. Component B is the underlying
    environment (providing resources) for A; B does not actively manage
    A (e.g. OS, VM).

-   **Class B: A-contained In-B**. Component A “lives in” B; B manages
    the lifecycle of A, from cradle to grave. (e.g. Widget in OWF; EJB
    in JEE server; OSGi bundle in Karaf; SCA).

-   **Class C: A-interfaces With-B**. Component A initiates
    communication with B via API(s). (e.g., JDBC, JMS, REST/SOAP call,
    legacy communications)

-   **Class D: A-indirectly Consumes-B**. Component A has a dependency
    o.n data/functionality of B even though it doesn't interface with B.
    (e.g. subscriber/publisher relationship; A integrates with another
    component that offers data from B).

Ports
-----

The applicable ports are shown below:

  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Port                                                                                                                                    Description                                                                      Type
                                                                                                                                                                                                                           
  (Defaults)                                                                                                                                                                                                               
  --------------------------------------------------------------------------------------------------------------------------------------- -------------------------------------------------------------------------------- -----------------------------------------------------------------
  8080                                                                                                                                    Tomcat HTTP                                                                      Inbound

  8009                                                                                                                                    Tomcat AJP                                                                       Inbound
                                                                                                                                                                                                                           
                                                                                                                                                                                                                           (Open if not using 8080)

  2424                                                                                                                                    OrientDB                                                                         Internal
                                                                                                                                                                                                                           
                                                                                                                                                                                                                           (Shouldn’t be exposed externally)

  2480                                                                                                                                    OrientDB                                                                         Internal
                                                                                                                                                                                                                           
                                                                                                                                                                                                                           (Shouldn’t be exposed externally)

  8983                                                                                                                                    ESA/Solr                                                                         Outbound
                                                                                                                                                                                                                           
                                                                                                                                                                                                                           (Used internally doesn’t need to be exposed outside the system)

  8080                                                                                                                                    OpenAM running on Tomcat; Setups on this vary so this just represents one case   Outbound
                                                                                                                                                                                                                           
                                                                                                                                                                                                                           (External application)

  All ports are configurable via configuration for the respected applications. Addition port maybe be using depending on configuration.
  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1.  Installation
    ============

    1.  High level instructions for a fresh install
        -------------------------------------------

Prior to the install, setup an ESA or Solr instance and make sure it's
running. Then, perform the following steps:

1.  Setup VM

2.  Install Java JDK 1.8

3.  Install Tomcat 7

4.  Integrate OpenAM Agent

5.  Deploy Application

6.  Configure Application

7.  Restart Tomcat (To pick up configuration changes)

8.  Import data

    1.  Suggested VM Configuration
        --------------------------

The following is the recommended VM configuration:

-   CentOS

-   CPUs: 4

-   RAM: 8GB

-   DISK: 40GB

-   Minimum:

    -   CPUs: 1

    -   RAM: 2GB (Application should be set to use 1GB)

    -   DISK: 20GB

    1.  Platform Dependencies
        ---------------------

The Storefront is dependent upon:

-   Java 8

-   Tomcat 7 &gt;v50

    1.  External Dependencies
        ---------------------

The Storefront relies upon the following external dependencies:

-   OpenAM (Optional)

-   ESA

-   Solr

**NOTE:** Base Solr will require some changes to the schema.xml to make
sure all field are available

### To Use Solr

Download Version 4.3.1
from <http://archive.apache.org/dist/lucene/solr/>, and then perform the
following steps:

1.  Unpackage

2.  Replace (Solr install dir)/example/solr/collection1/conf/schema.xml
    with the scheme.xml include in this project document folder.

3.  Configure OpenStorefront to point to Solr by going to:
    /var/openstorefront/config/openstorefront.properties

4.  Edit solr.server.url to
    solr.server.url=http://localhost:8983/solr/collection1

5.  Start Solr from (solr install dir)/example - java -jar start.jar

    1.  System Setup
        ------------

Unless otherwise noted, run as sudo.

**NOTE:** You can use Nano or another text editor.

### Install Java

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

-   \#Java Path

-   export PATH=\$PATH:/usr/java/latest/bin

-   export JAVA\_HOME=/usr/java/latest

a.  Save/Exit then source /etc/profile

b.  nano /etc/bash.bashrc

c.  Add to the bottom:

-   \#Java Path

-   export PATH=\$PATH:/usr/java/latest/bin

-   export JAVA\_HOME=/usr/java/latest

1.  Save/Exit then source /etc/bash.bashrc

2.  Confirm that java –version runs

    1.  ### Install Tomcat Public Package

Use the following steps to install the Tomcat public package.

1.  Download and copy to home directory

2.  tar -xvf apache-tomcat-7.0.55.tar.gz

3.  mv apache-tomcat-7.0.55 /usr/local/tomcat

4.  ln -s /usr/local/tomcat/apache-tomcat-7.0.55
    /usr/local/tomcat/latest

Use ln –nsf (target) (link) to repoint

5\. nano /usr/local/tomcat/latest/bin/setenv.sh

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

**OPTIONAL**: In the conf/server.xml

-   Add http compression on. Compression="on"

    Compressible
    MimeType="text/html,text/xml,text/plain,application/json,text/css"

> Also, you may consider setting more aggressive connection settings
> than the default depending on your expected load. See Tomcat
> documentation: <http://tomcat.apache.org/tomcat-7.0-doc/index.html>.
> The default settings will suffice for most deployments.

6\. Setup Tomcat as a service using the following:

http://www.davidghedini.com/pg/entry/install\_tomcat\_7\_on\_centos

a.  nano /etc/init.d/tomcat

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

a.  chmod 755 /etc/init.d/tomcat

b.  chkconfig --add tomcat

c.  chkconfig --level 234 tomcat on

7\. Open port iptables -I INPUT -p tcp --dport 8080 -j ACCEPT

### Install Tomcat Using JPackage

Use the following steps to install Tomcat using JPackage
(http://www.jpackage.org/)

1.  At www.jpackage.org, follow the installation instructions to setup
    the repository for your system install (e.g. yum, apt, and urpmi)

2.  After you have set up the repository, pull down Tomcat7-7.0.54-2
    from the version 6 repo.

**NOTE:** JPackage version has a mistake in the package. The ecj jar
library is old and doesn't work with JDK1.8.; however, this is easily
corrected. Download 7.0.54 from apache.org and replace ecj-xxx.jar in
tomcat/lib directory with ecj-xxx.jar from the 7.0.54 version.

### Server Control

Use the following commands to control the server.

-   service tomcat start

-   service tomcat stop

> **NOTE:** It can take a minute for the application to shut down.
> Please wait before restarting.

-   service tomcat restart

> **NOTE:** Using this is not recommended as it not always successful
> due the script not waiting for shutdown.

Deploying application
---------------------

To deploy the application, copy openstorefront.war to
/usr/local/tomcat/latest/webapps

Application Configuration
-------------------------

The application configuration and data are stored in
/var/openstorefront/. Make sure the user running the application has r/w
permission for that directory.  All directories are created upon
application startup. The high level directory map is stored under
/var/openstorefront/.

-   config - holds all configurations files. Defaults are created on
    initial startup.

-   db - holds database files

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

<!-- -->

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
> openam.header.group=memberOf
>
> openam.header.ldapguid=memberid
>
> openam.header.organization=
>
> openam.header.admingroupname=STORE-Admin

a.  Edit shiro.ini under the config directory

> under \[main\]
>
> uncomment (remove \#)
>
> headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm
>
> securityManager.realms = \$headerRealm
>
> under \[users\]
>
> comment out
>
> \#admin = secret, administrator
>
> \#user = user
>
> under \[roles\]
>
> comment out
>
> \#admin = administrator

Importing Data
--------------

Import data using the following steps.

1.  Export (or import) data from an existing system using application
    > admin tools. (Requires an Admin Login)

2.  When the application is first started, it will load a default set of
    “lookup” types. These can be later changed using the admin tools. On
    a new install, no attributes, articles or components are loaded.
    They can be entered or imported using the application admin tools.

    1.  Logging Notes
        -------------

You can view the logs messages in the Catalina log file:
/usr/local/tomcat/latest/logs

### Log Level Definitions

See the following table for the log definitions.

  **Level**   **Description**
  ----------- ------------------------------------------------------------------
  SEVERE      Something didn't run correctly or as expected
  WARN        Behavior may not be desired but, the system was able to continue
  INFO        System admin message
  FINE        Developer message
  FINER       Detailed developer messages
  FINEST      Trace information

Setup OpenAM
------------

See the example below for OpenAM setup. Your configuration may be
different.

(See https://bugster.forgerock.org/jira/browse/OPENAM-211: J2EE agents
are unable will not work, if the container was started prior to OpenAM).

**NOTE:** When the OpenAM policy agent is installed on Tomcat, the
application server will not start unless the OpenAM server is available.
This is a known issue with the OpenAM Policy agent.

### Versions Used

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

    1.  ### Installation of OpenAM Java EE Policy Agent into Tomcat 7.0.55

Use the following steps to install OpenAM Java EE Policy Agent on
Tomcat.

1.  Make sure the Agent Profile has already been created in OpenAM

2.  Create a pwd.txt file at C:\\Temp\\pwd.txt and add your Agent
    Profile password to it

3.  Shutdown the Tomcat server that is going to run your web application

4.  Make sure the Tomcat server that is running OpenAM is still running

5.  Extract Tomcat-v6-7-Agent-3.3.0.zip to a known directory

6.  CD into the j2ee\_agents/tomcat\_v6\_agent/bin directory

7.  Execute agentadmin --install to install the agent

    1.  ### References

-   <http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/index/chap-apache-tomcat.html>

-   <http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/#chap-apache-tomcat>

    1.  ### Configuration of OpenAM

See
<http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/getting-started/>
for OpenAM configuration information.

### Configure the Policy in OpenAM

Use the following steps to configure the OpenAM policy.

1.  Open up OpenAM in a web
    browser http://c00788.usurf.usu.edu:8080/openam

2.  Log into OpenAM using amadmin

3.  Click on Access Control &gt; /(Top Level Realm) &gt; Policies

4.  Click on New Policy

<!-- -->

a.  Give the Policy a name of Storefront Policy

b.  In the Rules table click New

<!-- -->

i.  Select URL Policy Agent and click **Next**

ii. Enter the following in Step 2 of 2: New Rule

-   Name: Allow Storefront Access

-   Resource Name: http://c00788.usurf.usu.edu:8081/agentsample/\*

-   Check the boxes for GET and POST

a.  In the Subjects table click **New**

<!-- -->

i.  Select Authenticated Users and click Next

ii. Name the rule All Authenticated Users

iii. Click **Finish**

<!-- -->

a.  Create a new response provider

-   In the Dynamic Attribute make sure uid and isMemberOf is
    selected (ctrl-click)

-   Click on **Finish**

    1.  ### Creating the Agent Profile

Use the following steps to create the agent profile.

1.  Open up OpenAM in a web
    browser http://c00788.usurf.usu.edu:8080/openam

2.  Log into OpenAM using amadmin

3.  Click on **Access Control** &gt; **Top Level Realm** &gt;
    **Agents** &gt; **J2EE**

4.  Create a new J2EE agent by clicking on the **New...** button under
    Agent

5.  Create the agent with the following parameters:

-   Name: myagent

-   Password: password

-   Configuration: Centralized

-   Server URL: http://c00788.usurf.usu.edu:8080/openam

-   Agent URL: http://c00788.usurf.usu.edu:8081/agentsample

1.  Configuration
    =============

    1.  Security
        --------

        1.  ### Supported Realms

Configure in /var/openstorefront/config/shiro.ini

-   INI (Properties File; Default)

    Users are specified in the users section.

-   LDAP (Example)

> \[main\]

-   ldapRealm = org.apache.shiro.realm.ldap.JndiLdapRealm

-   ldapRealm.userDnTemplate = uid={0},ou=users,dc=mycompany,dc=com

-   ldapRealm.contextFactory.url = ldap://ldapHost:389

-   ldapRealm.contextFactory.authenticationMechanism = DIGEST-MD5

-   ldapRealm.contextFactory.environment\[some.obscure.jndi.key\] = some
    value

    1.  #### Database

See
<http://stackoverflow.com/questions/17441019/how-to-configure-jdbcrealm-to-obtain-its-datasource-from-jndi>
for how to configure JDBCRealm to obtain its DataSource from JNDI.

\[main\]

dataSource = org.apache.shiro.jndi.JndiObjectFactory

dataSource.resourceName = java://app/jdbc/myDataSource

jdbcRealm = org.apache.shiro.realm.jdbc.JdbcRealm

jdbcRealm.permissionsLookupEnabled = true

jdbcRealm.dataSource = \$dataSource

\# you can customize the authenticationQuery, userRolesQuery and
permissionsQuery

\# if needed.

securityManager.realms = \$realm

#### OPENAM (Request Header) 

\[main\]

\#Also, remember to comment out the users and roles to remove the
INIRealm

headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm

securityManager.realms = \$headerRealm

#### Integration with OpenAM

Configure in: /var/openstorefront/config/openstorefront.properties

  ------------------------------------------------------------------------------------------------------------
  Property                       Description                                                  Default
  ------------------------------ ------------------------------------------------------------ ----------------
  openam.url                     http:/.../openam (Full URL to open am instance)              

  logout.url                     http:/.../openam/UI/Logout                                   
                                                                                              
                                 Full URL to logout                                           

  openam.header.username         HTTP Header for Username                                     sAMAccountName

  openam.header.firstname        HTTP Header for Firstname                                    givenname

  openam.header.lastname         HTTP Header for Lastname                                     sn

  openam.header.email            HTTP Header for email                                        mail

  openam.header.group            HTTP Header for group                                        memberOf

  openam.header.ldapguid         HTTP Header for ldapguid                                     memberid

  openam.header.organization     HTTP Header for organization                                 

  openam.header.admingroupname   HTTP Header for Admin Group Name \*Handles multiple values   STORE-Admin
  ------------------------------------------------------------------------------------------------------------

### User Types

The user types are:

-   User: This is restricted user that constitutes a normal user of
    the application.

-   Administrator: This is an unrestricted user that can use the
    administrator tools in the application.

    1.  Integration External LDAP (User Syncing)
        ----------------------------------------

When a user is not located in the external management system then the
user profile in the application will be deactivated.

Warning: This will not prevent login! Upon login the user profile will
be reactivated. To prevent login, refer to the external user management
system that the application is connected to and inactive the user from
there.

Configure in: /var/openstorefront/config/openstorefront.properties

  Property                              Description                                                                         Default
  ------------------------------------- ----------------------------------------------------------------------------------- ----------------
  ldapmanager.url                       Full URL to the LDAP (ldap://ldapHost:389 or ldap://localhost:389/o=JNDITutorial)   
  ldapmanager.userDnTemplate            uid={0},ou=users,dc=mycompany,dc=com; Reserved, not currently used                  
  ldapmanager.authenticationMechanism   NONE, SIMPLE, DIGEST-MD5, etc.                                                      SIMPLE
  ldapmanager.security.sasl.realm       May be needed for SASL authentication                                               
  ldapmanager.binddn                    The LDAP user to use in the connection (Full DN name)                               
  ldapmanager.credentials               The LDAP credentials                                                                
  ldapmanager.contextRoot               Root to directory to search                                                         
  ldapmanager.attribute.username        Attribute to map to username                                                        sAMAccountName
  ldapmanager.attribute.email           Attribute to map to email                                                           mail
  ldapmanager.attribute.fullname        Attribute to map to fullname                                                        name
  ldapmanager.attribute.organization    Attribute to map to organization                                                    company
  ldapmanager.attribute.guid            Attribute to map to guid                                                            objectGUID

Jira Integration
----------------

Configure in: /var/openstorefront/config/openstorefront.properties

  Property                       Description                                                    Default
  ------------------------------ -------------------------------------------------------------- -----------------------
  tools.login.user               Login Credentials for Integrations (currently just for jira)   
  tools.login.pw                 Login Credentials for Integrations (currently just for jira)   
  jira.connectionpool.size       Resource pool size                                             20
  jira.connection.wait.seconds   Wait time if the pool is empty                                 60
  jira.server.url                Jira server to connect to                                      https://jira.di2e.net

Mail Server
-----------

Configure in: /var/openstorefront/config/openstorefront.properties

  Property             Description                                                    Default
  -------------------- -------------------------------------------------------------- ---------------------------
  mail.smtp.url        Login Credentials for Integrations (currently just for jira)   localhost
  mail.server.user     Login Credentials for mail server                              
  mail.server.pw       Login Credentials for mail server                              
  mail.smtp.port       Mail Port (25 common)                                          
  mail.use.ssl         Set to “true” if server requires it                            
  mail.use.tls         Set to “true” if server requires it                            
  mail.from.name       From Name                                                      Storefront Notification
  mail.from.address    From Email Address                                             donotreply@storefront.net
  mail.reply.name      Reply name (usually display at the bottom the message)         Support
  mail.reply.address   Reply email (usually display at the bottom the message)        helpdesk@di2e.net

Other Application Properties
----------------------------

Configure in: /var/openstorefront/config/openstorefront.properties

  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  Property                             Description                                                                                                                                                                Default
  ------------------------------------ -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- --------------------------------
  errorticket.max                      Max amount of ticket to hold (culls the oldest records upon filling)                                                                                                       5000

  trackingrecords.max.age.days         Max age of tracking records                                                                                                                                                365

  solr.server.url                      URL to the SOLR instance to use.                                                                                                                                           http://localhost:8983/solr/esa
                                                                                                                                                                                                                  
                                       NOTE: it should point to the appropriate collection.                                                                                                                       

  db.connectionpool.min                DB min pool size                                                                                                                                                           5

  db.connectionpool.max                DB max pool size                                                                                                                                                           40

  db.user                              Should match orientdb-server-config.xml                                                                                                                                    

  db.pw                                Should match orientdb-server-config.xml                                                                                                                                    

  job.working.state.override.minutes   Max job running time. Use for Integrations. To determine if a job got stuck.                                                                                               30

  message.archive.days                 User message max age of archives                                                                                                                                           30

  message.queue.minmintues             User message queue time or the time the message waits before sending.                                                                                                      10

  message.maxretires                   Max times the user message will try to send if unable to deliver.                                                                                                          5

  message.recentchanges.days           Time between “recent changes” messages from being sent.                                                                                                                    28

  app.title                            Title of the application. Used in emails but, also other places.                                                                                                           DI2E Storefront

  external.usermanager                 Specifies the manager that is used for external user management. The manager must be supported by the application. ( IniRealmManager or LdapUserManager)                   IniRealmManager

  external.sync.activate               Set to ‘true’ to run the sync                                                                                                                                              False

  dblog.on                             Activates logging records to the database; Note: All log record are still logged in the server logs regardless of setting this. This just controls the database logging.   True

  dblog.maxrecords                     Maximum database records to store                                                                                                                                          100000

  dblog.logSecurityFilter              Log security API audit records; Note: setting this to true can cause noise when using the application log viewer.                                                          False
  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Database Management
===================

[**DATABASE
REFRESH**](https://confluence.di2e.net/display/STORE/Database+Refresh)**:**
The application handles all database interaction transparently, so
direct database access and manipulation is not needed.  

See the following for information on outside control (should rarely be
needed/used).

Refreshing the Database
-----------------------

**CAUTION:** This will wipe out all data in the application. Data, such
as User profiles, cannot be recovered. Component user data can be
preserved by performing an export from the component admin tool.

Make a backup by copying all of the files in the /var/openstorefront/db
directory or use the following console tools steps:

1.  Stop the Tomcat server  (e.g. service tomcat stop)

2.  Remove the folder /var/openstorefront/db
     (rm -rf /var/openstorefront/db)

3.  Start the tomcat server

When the application loads it will create a new database and populate
the data from whatever is currently in the import folders (Attributes
and lookups only- component and articles will need to be manually
trigger or uploaded via the Admin Tools UI ).   

The initial load of the application may take a few minutes. If the
import directories are empty, the application will load default lookup
and attributes files that are packaged with the application.

 Installing Database Console
----------------------------

**CAUTION:** Viewing (Querying) information is fine; however, use
extreme caution when modifying any records as all logic is handled by
the application.

1.  Download Orient DB (Currently using the 1.7.x series) at
    <http://www.orientechnologies.com/download/>

<!-- -->

2.  Extract the archive

3.  Run the console ./bin/console.sh 

4.  Connect to the DB. Connect remote: localhost/openstorefront
    &lt;user&gt; &lt;password&gt;  (see the
    /var/openstorefront/config/openstorefront.properties for
    connection information)

The database supports an SQL like interface and then adds other
functionality on top.

-   See <http://www.orientechnologies.com/docs/last/orientdb.wiki/Backup-and-Restore.html> for
    information about backup

-   See <http://www.orientechnologies.com/docs/last/orientdb.wiki/Export-and-Import.html> for
    export and imports.

External Application API
========================

The API document is directly reflected from the live code so it is
always current for the running version of the application. The API
documentation can be accessed by login in as an admin and following the
link from the admin tools. A print view of the API can be generated form
there as well.

1.  Development
    ===========

    1.  Development Client
        ------------------

        1.  ### Quick start

If you want to contribute to this project and you know Git, Yoeman,
Bower, and Grunt, these build instructions should suffice:

1.  To build Open-Storefront:

> \$ git clone https://github.com/dshurt/Open-Storefront.git
>
> \$ cd Open-Storefront
>
> \$ grunt build

1.  Install the following:

-   Git 1.9.4

-   npm (node.js): 1.4.13

-   yoeman and generator-angular (yoeman vanilla
    [generator-angular](https://github.com/yeoman/generator-angular):
    <https://github.com/yeoman/generator-angular>)

-   bower

-   grunt

-   ruby (with sass and compass)

    1.  #### Installing Components

    2.  #### Installing and Configuring Git

Use the following steps to install and configure Git on your OS:

1.  Linux: Install the package Git using \$ sudo apt-get install git

2.  Tip, also install gitk* *to visualize your Git log using \$ sudo
    apt-get install gitk

-   Windows, Mac OSX: Download from: <http://git-scm.com/>

1.  Tip for Mac OSX: Also
    install [GitX](file:///\\hera\C4ISR_DSP\NRO\DI2E\Storefront\documentation\GitX) (<http://gitx.frim.nl/>)
    to visualize your Git log.

-   More info [in github's git installation
    > instructions](http://help.github.com/git-installation-redirect):
    > <https://help.github.com/articles/set-up-git/>

1.  Check if Git is installed correctly:

    \$ git --version

    git version 1.7.1

<!-- -->

a.  Configure Git correctly:

    \$ git config --global user.name "My Full Name"

    \$ git config --global user.email myAccount@gmail.com

    \$ git config --global -l

    user.name=Geoffrey De Smet

    user.email=gds...@gmail.com

WARNING: the field user.name is your full name, not your username*.*

-   NOTE: the field user.email should match an email address of your
    GitHub account.

-   More info on GitHub:
    <https://help.github.com/articles/setting-your-email-in-git/>.

a.  Get a GitHub account

-   And add your public key on GitHub, using the instructions here:
    <https://help.github.com/articles/generating-ssh-keys/>

-   To learn more about Git, read the free book [Git
    Pro](http://progit.org/book/) (<http://git-scm.com/book/en/v2>).

    1.  #### Getting the Sources Locally

You should fork the code before changing or cloning it (recommended).
This will make it easier to share your changes later. For more info on
forking, read [GitHub's help on
forking](file:///\\hera\C4ISR_DSP\NRO\DI2E\Storefront\documentation\GitHub's%20help%20on%20forking)
(<https://help.github.com/articles/fork-a-repo/>).

To fork the code:

1.  For example, Open-Storefront:

2.  Go to [the specific
    repository (Open-Storefront)](https://github.com/dshurt/Open-Storefront)

3.  Click the top right **Fork** button 

NOTE: By forking the repository, you can commit and push your changes
without our consent and we can easily review and then merge your changes
into the blessed repository.

#### Clone a Fork Locally

Use the following steps to locally clone a fork.

\# First make a directory to hold the Open-Storefront project

> \$ mkdir Open-Storefront-Project
>
> \$ cd Open-Storefront-Project

\# Then clone the repository you want to clone.

> \$ git clone https://github.com/di2e/openstorefront.git
>
> \$ cd Open-Storefront
>
> \$ ls

WARNING: You can clone with the *SSH URL*. It is possible that
the *HTTPS URL* can be unreliable.

NOTE: It's recommended to name the cloned directory the same as the
repository (the default), so the helper scripts work.

1.  By default you will be looking at the sources of the master branch,
    which can be very unstable.

2.  Use Git checkout \$ git checkout 5.2.0.Final to switch to a more
    stable branch or tag.

    1.  #### Share your Changes with a Pull Request

A pull request is like a patch file, but easier to apply, more powerful
and you'll be credited as the author.

1.  Creating a pull request:

<!-- -->

a.  Push all your commits to a topic branch on your fork on GitHub (if
    you haven't already).

-   You can only have one pull request per branch, so it's advisable to
    use topic branches to avoid mixing your changes.

a.  Surf to that topic branch on your fork on GitHub.

b.  Click the **Pull Request** button at the top of the page.

<!-- -->

1.  Accepting a pull request:

<!-- -->

a.  Surf to the pull request page on GitHub.

b.  Review the changes

c.  Click the **Merge help** button on the bottom of the page and follow
    the instructions of GitHub to apply those changes on the master.

-   Or, use the **Merge** button if there are no merge conflicts.

    1.  ### Installing and Configuring NPM

Installing node.js (<https://nodejs.org/>) on your computer will also
install the node package manager (npm), but if you'd rather just go
straight to the source, you can follow the
instructions (<https://raw.githubusercontent.com/npm/npm/master/README.md>)
in order to install the npm manually.

No special configuration is required, but details on npm configuration
can be found at <https://docs.npmjs.com/cli/config>.

### Installing and Configuring Yoeman, Bower, and Grunt

Use the following steps to insall and configure Yeoman, Bower, and
Grunt.

1.  To install Yeoman, enter the following:

-   \# The -g installs yoeman globally

-   \$ npm install -g yo

1.  Once yeoman has finished installing, then install the AngularJS
    scaffolding tool for Yeoman:

-   \# Once again the -g installs the generator globally so that you can
    use it anywhere inside a yoeman project

-   \$ npm install -g generator-angular

You can now start scaffolding your apps with Yeoman, managing
dependencies with Bower, and building and running your application with
Grunt. Grunt and Bower have also been installed globally, so you should
be able to use them in other projects from now on.

### Notes for Redhat/Centos users:

See the following notes for Redhat/Centos users.

-   npm install /-g grunt-cli (was prompted by npm to install)

-   npm install grunt-contrib-compass --save-dev

-   npm install grunt-bower-install --save-dev

-   npm install grunt-wiredep --save-dev

-   yum install ruby yum install ruby-devel

-   gem install sass gem install compass

-   npm install karma --save-dev

-   npm install karma-coverage --save-dev

-   npm install karma-jasmine --save-dev

-   npm install karam-chrome-launcher --save-dev

-   yum install google-chrome-stable

-   yum install maven

    1.  ### Building with Grunt

        1.  #### Before Running the Build

Before running the build, make sure all of the correct dependencies
installed.

-   \#navigate to client/openstorefront/

-   \$ cd
    \~/projects/Open-Storefront-Project/Open-Storefront/client/openstorefront/

-   \#and run npm install

-   \$ npm install

This should create a node\_modules folder inside of the
client/openstorefront/ directory with all of the node modules you'll
need to do a build.

It will also create a folder within client/openstorefront/app/ called
'bower\_components' with all of the required bower components for the
site.

**NOTE:** If Bower is not installed correctly (globally), you will run
into issues with the npm install creating the Bower components.

#### Running the build

Go into a project's front end base directory, for example Open-

-   Storefront/client/openstorefront:

    -   \$ cd
        \~/projects/Open-Storefront-Project/Open-Storefront/client/openstorefront

    -   \$ ls

    -   app/ bower.json\* Gruntfile.js\* karma.conf.js\*
        karma-e2e.conf.js\* node\_modules/ package.json\* test/

-   Run the build:

    -   \$ grunt build --appPath=/openstorefront

        or use

    -   \$ grunt buildprod

"appPath" is only needed when changing the root context. The first build
will take a long time, because many dependencies will be downloaded (and
cached locally). The first build might even fail (if certain servers are
offline) or experience hiccups. In that case, you'll see an IO error, so
just run the build again. After the first successful build, any
subsequent build should be fast and stable.

#### Running tests

Open-Storefront uses Karma to run tests for the frontend, so those tests
need to be run differently than others.

-   \$ cd \~/projects/Open-Storefront-Project/Open-Storefront/frontend

-   \$ grunt test

    1.  Development Server
        ------------------

        1.  ### Key Components Used

The following components were used in the development:

-   JDK 8

-   ESA/Solr

-   OpenAM (Configurable)

The application is a JEE webapp, so any JEE 6 (web-profile) compliant
server should work with some server configuration. The current
deployment target is Tomcat 7.

### Key Libraries Used

The following key libraries were used in the development:

-   JAX-RS- heavily used for REST API. (Jersey with Moxy for
    data binding)

-   Stripes- Action based web framework

-   Jackson- JSON Handling/Binding

-   Apache Shiro- Security

-   Orient DB- No SQL/Multi-Model database

    1.  ### Building with Maven

run "mvn install" from \$PROJECT\_HOME/server/openstorefront

(Skip tests)\
Mav -Dmaven.test.skip=true or -DskipTests=true install

### Deploying

Copy the war artifact to the webapp directory for Tomcat. Some IDEs can
handle this. See application server documentation for other deployment
mechanisms.

### Building 

This puts the client and server pieces together for a simple deployment.

1.  Build Client from client/openstorefront/

<!-- -->

a.  npm install

b.  grunt build-prod or grunt build-debug for ease debugging

    1.  Build Server from server/openstorefront/

<!-- -->

a.  mvn install

    1.  Running
        -------

The application is targeted to run in Tomcat 7; however, it may run in
other compatible containers with little or no changes.

**NOTE:** Searching requires an external ESA/(Solr) instance setup.

Setting up Solr
---------------

ESA uses Solr 4.3.1, so the application is setup to use that specific
version.

Download Version 4.3.1
from <http://archive.apache.org/dist/lucene/solr/> and perform the
following steps:

1.  Unpackage

2.  Replace (solr install dir) /example/solr/collection1/conf/schema.xml
    with the scheme.xml include in this project’s doc folder.

3.  configure openstorefront to point to Solr

    a.  /var/openstorefront/config/openstorefront.properties

4.  edit solr.server.url to
    solr.server.url=http://localhost:8983/solr/collection1

5.  Start Solr from (solr install dir)/example - java -jar start.jar

    1.  Testing
        -------

-   Unit test run as part of the Maven install.

-   Container/Integration tests login as admin go to
    <http://localhost:8080/openstorefront/test/ServiceTest.action>

-   Automated end to end testing: run from client/openstorefront/

    -   npm test (requires Chrome Web Browser to be installed)

    1.  Contributing Patches
        --------------------

The code is hosted on the public GitHub
(<https://github.com/di2e/openstorefront>). Create a pull request to the
current release branch. A pull request will be reviewed prior to merge.
Please file bugs or enhancement by submitting a ticket to:

<https://jira.di2e.net/browse/STORE> (Login required)

If you are unable to obtain a login account then submit an issue ticket
on the GitHub site.

Versioning Strategy
-------------------

The software is versioned based on the following:

Major.Minor.Patch

A major version change is represents a major change in functionality or
an addition of a major new feature. A minor version change represent
minor features and improvement along with bug fixes. A patch release is
done only for bug fixes and needed improvements to existing features.

The REST API versioning follows <http://semver.org/> where major version
represents incompatible API changes. The REST endpoints include a
version number which represents the major version. The version in the
URL doesn’t change with minor versions. However, the API follows with
the version of the application.
