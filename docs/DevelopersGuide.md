
# Developer Guide

#1. Internal Development
------

See documents under /dev for Coding and Style guides.

##1.1 Key Components Used

The following components were used in the development:

-   JDK 8

-   Elasticsearch/Solr

-   OpenAM (Configurable, default is property based log in.)

The application is a JEE servlet-container webapp, so any JEE 6 servlet-container
compliant server should work with some server configuration. The current
deployment target is Tomcat 7.


##1.2 Key Libraries Used

The following key libraries were used in the development:

-   JAX-RS- heavily used for REST API. (Jersey with Moxy for
    data binding)

-   Stripes- Action based web framework

-   Jackson- JSON Handling/Binding

-   Apache Shiro- Security

-   Orient DB- No SQL/Multi-Model database

-   Ext.js and tinymce

##1.3 Dev Environment on Windows with NetBeans

1. **Install dependendies**
  * Install NetBeans - https://netbeans.org/downloads/
  * Install TomCat 7 - http://tomcat.apache.org/download-70.cgi
  * Install Maven - http://maven.apache.org/install.html
2. **Clone the openstorefront GitHub repo to the desired directory**
3. **Intial NetBeans configuration**
  * Select Tools -> Plugins. Once the plugins window opens, select the "Available Plugins" tab
  * Search for "Java EE Base", check it, and select install. Restart NetBeans if prompted
  * Select File -> Project Groups then double-click on "(none)"
4. **Open the openstorefront Project**
  * Select the Open Project Icon or press CTRL + SHIFT + O
  * Open the openstorefront GitHub repo on your local machine
11. **TomCat Server configuration**
  * Select Tools -> Servers. Once the servers window opens, select Add Server
  * Follow the wizard to select the TomCat server location and add a user
  * Once created, select the "Startup" tab and ensure the "Socket Port" option is selected.
  * Select the Platform tab and set the VM options to use at least 2GB of ram by entering -Xmx[memory size]. Ex. -Xmx2g for 2GB of memory



-----


##1.4 Building with Maven

run "mvn install" from \$PROJECT\_HOME/server/openstorefront

(Skip tests)
Mav -Dmaven.test.skip=true or -DskipTests=true install

Note: To install maven on RedHat/CentOS run the following:

-   yum install maven

##1.5 Deploying

Copy the war artifact to the webapp directory for Tomcat. Some IDEs can
handle this for you. See application server documentation for other deployment
mechanisms.

##1.6  Running
-------

The application is targeted to run in Tomcat 7; however, it may run in
other compatible containers with little or no changes.

**NOTE:** Searching requires an external Elasticsearch/(Solr) instance setup.
See [Setup](Setup.md)


##1.7 Testing
-------

-   Unit tests run as part of the Maven install.

-   Container/Integration tests login as admin go to
    <http://localhost:8080/openstorefront/test/ServiceTest.action>


##1.8  Contributing Patches
--------------------

The code is hosted on the public GitHub
[https://github.com/di2e/openstorefront](<https://github.com/di2e/openstorefront>). Create a pull request to the
current release branch. A pull request will be reviewed prior to merge.
Please file bugs or enhancement by submitting a ticket to:

<https://jira.di2e.net/browse/STORE> (Login required)

If you are unable to obtain a login account then submit an issue ticket
on the GitHub site.

##1.9 Versioning Strategy
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
URL doesn't change with minor versions. However, the API follows with
the version of the application.

##1.10 Licensing
-----------------

The project as a whole (front-end and server code) is GPL V3 but, individual parts may use compatible licenses. This is in compliance with the licensing.  Mark UI code that uses EXT JS with the GPL header.  Mark server code and other code as Apache V2. See NOTICE.txt for more information.  Our goal is allow for broader usage when other requirements are met.  This also claries how individual pieces can be used.

#2. External Developers
------

This guide is targeted at external developers who want to extend the application.

##2.1 REST API

The API document is directly reflected from the live code so it is always current for the running version of the application.
The API documentation can be accessed by login in as an admin and following the link from the admin tools see application management.
A print view of the API can be generated form there as well.

##2.2 Adding Custom Parser (Plugin)
----

A custom parser may be need for handling complex formats that can't be support via data mapping.
In some cases, both a custom parser and data mapping may be required.

1 - Create an OSGi bundle (you use a maven project)

2 - Add a dependency to openstorefront-core-api

3 - In the Activator.java, register your new parser
   (More than one parser can be added to a plugin)
>In start bundle:
>
    FileFormat customFormat = new FileFormat();
	customFormat.setCode("CUSTOMFORMATCODE");
	customFormat.setFileType(FileType.COMPONENT);
	customFormat.setDescription("Custom format");
	customFormat.setSupportsDataMap(true);  //Set to true to allow data mapping
	customFormat.setParserClass(CustomParser.class.getName());

>	service.getImportService().registerFormat(customFormat, CustomParser.class);

>   In stop bundle:

>   service.getImportService().unregisterFormat(customFormat.class.getName());

4 - Upload build JAR using the Admin->Application Management->System->Plugins
or copy jar to /var/openstorefront/perm/plugins and the application will auto-deploy

**Note:**  Only Libraries and API the application expose are available. (CORE-API, CORE-COMMON)
All other third-party libraries must be included with your JAR.

###2.2.2 Parser Workflow

(Default flow but it can be overridden)
1 - Check Format (On Web Upload)

2 - Process Data

> a) Get the parser Reader (CSV, Text, XML...etc)

> b) Read Record

> c) Validate Record

> d) Add record to storage batch (flushes batch if full)

> e) Loop through remaining records

> f) Flush any records not stored

> g) Finish Processing (Override for special handling; Eg Media and Relationships)


###2.2.3 Parser Class

1 - Extend Either the Component or Attribute Base Parser

2 - Implement Check format and parse record

Override other method such as getReader as needed.
The parse record method receive a model according to the reader.
Then it should return a record such as ComponentAll to be stored.
If the method return null it will skip the record.

**Note:** The developer has access to the filehistory record and the service proxy.

**See:** spoon importer plugin as a example.
