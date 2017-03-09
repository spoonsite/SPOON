
# Developer Guide
-------

#1. Internal Development

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

##1.3 Dev Environment with NetBeans

1. **NetBeans Install/configuration**
  * Install NetBeans with Java EE and Tomcat - https://netbeans.org/downloads/
    * Select "Customize" on the NetBeans installer to switch the server from GlassFish to Tomcat
  * If you already have NetBeans installed
    * In the Tools -> Plugins window check for "Java EE Base" install it if not already installed. Restart NetBeans if prompted
    * Install Tomcat 7 (if not already installed) - http://tomcat.apache.org/download-70.cgi
2. **Clone the openstorefront GitHub repo to the desired directory**
  * https://help.github.com/articles/cloning-a-repository/
3. **Open the openstorefront Project**
  * Select the Open Project Icon or press CTRL + SHIFT + O
  * Open the openstorefront/server directory
  * Select the "openstorefront" option and make sure "Open Required Projects" is selected
4. **Tomcat Server configuration**
  * Select Tools -> Servers. Once the servers window opens, select Add Server
  * Follow the wizard to select the Tomcat server location and add a user
  * Once created, select the "Startup" tab and ensure the "Socket Port" option is selected.
  * Select the Platform tab and set the VM memory options to -Xmx[memory size].
    * We recommend at least 2GB. (-Xmx2g)
  * if you want to use the Tomcat manager-gui you will need to configuring manager application access -  https://tomcat.apache.org/tomcat-7.0-doc/manager-howto.html#Configuring_Manager_Application_Access
    * make sure you have RW access to $Tomcat_HOME/bin/felix-cache

##1.4 Building with Maven CLI

Install Maven if not installed by your IDE - http://maven.apache.org/install.html

run "mvn install" from \$PROJECT\_HOME/server/openstorefront

(Skip tests) mvn -Dmaven.test.skip=true or -DskipTests=true install


##1.5 Deploying

Copy the war artifact to the webapp directory for Tomcat. Some IDEs can
handle this for you. See application server documentation for other deployment
mechanisms.

##1.6  Running

The application is targeted to run in Tomcat 7; however, it may run in
other compatible containers with little or no changes.

**NOTE:** Searching requires an external Elasticsearch/(Solr) instance setup.
See [Setup](Setup.md)


##1.7 Testing

-   Unit tests
  * run as part of the Maven install.
  * NetBeans, Eclipse and IntelliJ Idea have native graphical test runners built in.
  * with the JUnit CLI java org.junit.runner.JUnitCore [class to test] http://junit.org/junit4/faq.html#atests_1


-   Container/Integration tests login as admin go to
    <http://localhost:8080/openstorefront/test/ServiceTest.action>


##1.8  Contributing Patches

The code is hosted on the public GitHub
[https://github.com/di2e/openstorefront](<https://github.com/di2e/openstorefront>). Create a pull request to the
current release branch. A pull request will be reviewed prior to merge.
Please file bugs or enhancement by submitting a ticket to:

<https://jira.di2e.net/browse/STORE> (Login required)

If you are unable to obtain a login account then submit an issue ticket
on the GitHub site.

##1.9 Versioning Strategy

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

The project as a whole (front-end and server code) is GPL V3 but, individual parts may use compatible licenses. This is in compliance with the licensing.  Mark UI code that uses EXT JS with the GPL header.  Mark server code and other code as Apache V2. See NOTICE.txt for more information.  Our goal is allow for broader usage when other requirements are met.  This also clarifies how individual pieces can be used.

##1.11 Security
-----------------

Openstorefront support several environments each have different security needs.
It also support a built in user management.  Regardless of the authentication mechanism the security is based on dynamic role made up of permissions.

The granularity of the permissions is mostly feature/tool based.  

###1.11.1 Adding Permissions

1. Add Permission to SecurityPermission entity in code.

2. Add Permission to auto-generated admin group (See Core-Service-> ...core.init.SecurityInit.java)

3. Apply Permission


Two place where permission need to be applied:

_ Server API

_ UI (typically to restrict a page however, it may be used to restrict a piece of functionality)

Use caution in marking APIs as there may be other features that rely a shared API to work correctly. Should be a rare case.
Also, keep in mind there may be special handling for "owners" of the data beyond a permission.


###1.11.2 Data Restriction

Data may be restricted by source and/or data sensitivity.  Data sensitivity may 
be marked at a entity-level however, not all entity need to be marked. Marking
support on the UI may be set according to needs.

All Data-based API need to handling filtering data.


#2. External Developers

This guide is targeted at external developers who want to extend the application.

##2.1 REST API

The API document is directly reflected from the live code so it is always current for the running version of the application.
The API documentation can be accessed by logging in as an admin and following the link from the admin tools see application management.
A print view of the API can be generated form there as well.

##2.2 Adding Custom Parser (Plugin)

A custom parser may be needed for handling complex formats that can't be support via data mapping.
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
