
# Clearinghouse Administrator Guide

Version 2.4


Space Dynamics Laboratory

Utah State University Research Foundation

1695 North Research Park Way

North Logan, Utah 84341

![logo](images/sdl.png)


# Overview
-----

The Open Storefront application is a software cataloging system that is used to catalog components
of interest to the DI2E community. Components include Government off
the shelf (GOTS), commercial off the shelf (COTS), and Open Source
software (OSS). The component evaluations done by DI2E's Centers of
Excellence are displayed in the Storefront and give details on the
following:

-   Ownership

-   Where/How to access the software

-   Software vitals

-   Conformance

-   Links to documentation, source code and other artifacts

-   Evaluation information

**Open Storefront is developed by Space Dynamics Laboratory and is
licensed under Apache V2.**

# 1. Other Guides
----

This guide has been organized into separate documents to make it easier to navigate.
The focus of this document is for configuration.  See other guides for other topics.

[Architecture](Architecture.md)

[Setup](Setup.md)

[Developer Guide](DevelopersGuide.md)

# 2.  Configuration
------

## 2.1  Security
--------

### 2.1.1 Supported Realms
------

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

#### 2.1.1.1 Database

See
[Configure JDBC Realm](http://stackoverflow.com/questions/17441019/how-to-configure-jdbcrealm-to-obtain-its-datasource-from-jndi)
for how to configure JDBCRealm to obtain its DataSource from JNDI.

> \[main\]

-  dataSource = org.apache.shiro.jndi.JndiObjectFactory

-  dataSource.resourceName = java://app/jdbc/myDataSource

-  jdbcRealm = org.apache.shiro.realm.jdbc.JdbcRealm

-  jdbcRealm.permissionsLookupEnabled = true

-  jdbcRealm.dataSource = \$dataSource

 \# you can customize the authenticationQuery, userRolesQuery and
permissionsQuery,  if needed.

- securityManager.realms = \$realm

#### 2.1.1.2 OPENAM (Request Header)

>\[main\]

\#Also, remember to comment out the users and roles to remove the INIRealm

- headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm

- securityManager.realms = \$headerRealm

#### 2.1.1.3 Integration with OpenAM
-----

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

-  **openam.url**  -http:/.../openam (Full URL to open am instance)              

-  **logout.url** - http:/.../openam/UI/Logout   (Full URL to logout)   

-  **openam.header.username**  - HTTP Header for Username    ( **sAMAccountName** )   

-  **openam.header.firstname**  - HTTP Header for Firstname    ( **givenname** )        

-  **openam.header.lastname** - HTTP Header for Lastname      ( **sn** )      

-  **openam.header.email**  - HTTP Header for email      ( **mail** )      

-  **openam.header.group**  - HTTP Header for group      ( **memberOf** )      

- **openam.header.ldapguid** - HTTP Header for ldapguid      ( **memberid** )      

-  **openam.header.organization** - HTTP Header for organization    

-  **openam.header.admingroupname** - HTTP Header for Admin Group Name \*Handles multiple values  ( **STORE-Admin** )    

Also, need to adjust the open am agent filter

Change:

    <filter-mapping>
        <filter-name>Agent</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>INCLUDE</dispatcher>
        <dispatcher>FORWARD</dispatcher>
        <dispatcher>ERROR</dispatcher>
    </filter-mapping>

To

    <filter-mapping>
        <filter-name>Agent</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>ERROR</dispatcher>
    </filter-mapping>


### 2.1.2 User Types
-----

The user types are:

Are defined as based on the roles they belong to.  
If using the built-in security then the system will create default roles for
default users, admins and evaluators.  It will create a default admin user: (admin / Secret1@)

**WARNING** You should change the admin password after login in.


## 2.2  Integration External LDAP (User Syncing)
----------------------------------------

When a user is not located in the external management system then the
user profile in the application will be deactivated.

The Security Policy in the application now allows for disabling user information editing. (Optional)
This allow the external LDAP/Active Directory to be sole source of user information.
Information will be synced at login and periodically according to teh user sync job.

Warning: This will not prevent login! Upon login the user profile will
be reactivated. To prevent login, refer to the external user management
system that the application is connected to and inactive the user from
there.

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

Also need to set the following properties to activate the feature:
-	**external.sync.activate**			- Set to true to activate
-	**external.usermanager**			- LdapUserManager


-   **ldapmanager.url**                       -Full URL to the LDAP (ldap://ldapHost:389 or ldap://localhost:389/o=JNDITutorial)   
-   **ldapmanager.userDnTemplate**   -         uid={0},ou=users,dc=mycompany,dc=com; Reserved, not currently used                  
-   **ldapmanager.authenticationMechanism** -  NONE, SIMPLE, DIGEST-MD5, etc.                                                      ( **SIMPLE** )
-   **ldapmanager.security.sasl.realm** -      May be needed for SASL authentication                                               
-   **ldapmanager.binddn** -                    The LDAP user to use in the connection (Full DN name)                               
-   **ldapmanager.credentials** -               The LDAP credentials                                                                
-   **ldapmanager.contextRoot** -               Root to directory to search                                                         
-   **ldapmanager.attribute.username** -        Attribute to map to username                                                       ( **sAMAccountName** )
-   **ldapmanager.attribute.email** -           Attribute to map to email                                                           ( **mail** )
-   **ldapmanager.attribute.fullname** -        Attribute to map to fullname                                                        ( **name** )
-   **ldapmanager.attribute.organization** -    Attribute to map to organization                                                    ( **company** )
-   **ldapmanager.attribute.guid** -            Attribute to map to guid                                                            ( **objectGUID** )

## 2.3 Jira Integration
----------------

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

-  **tools.login.user** -               Login Credentials for Integrations (currently just for jira)   
-  **tools.login.pw** -                 Login Credentials for Integrations (currently just for jira)   
-  **jra.connectionpool.size** -       Resource pool size                                             ( **20** )
-  **jira.connection.wait.seconds** -   Wait time if the pool is empty                                 ( **60** )
-  **jira.server.url** -                Jira server to connect to                                      ( **https://jira.di2e.net** )

## 2.4 Confluence Integration
----------------

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

-  **confluence.server.url** -URL to confluence


## 2.5 Mail Server
-----------

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

-  **mail.attach.file** -     Set to true or false indicating if emails should contain attached files
-  **mail.smtp.url** -        Login Credentials for Integrations (currently just for jira)   ( **localhost** )
-  **mail.server.user** -     Login Credentials for mail server                              
-  **mail.server.pw** -       Login Credentials for mail server                              
-  **mail.smtp.port** -       Mail Port (25 common)                                          
-  **mail.use.ssl** -         Set to true if server requires it                            
-  **mail.use.tls** -         Set to true if server requires it                            
-  **mail.from.name** -       From Name                                                      ( **Storefront Notification** )
-  **mail.from.address** -    From Email Address                                             ( **donotreply@storefront.net** )
-  **mail.reply.name** -      Reply name (usually display at the bottom the message)         ( **Support** )
-  **mail.reply.address** -   Reply email (usually display at the bottom the message)        ( **helpdesk@di2e.net** )
-  **test.email** -           Set for automated testing only; the email to use for testing

## 2.6 Other Application Properties
----------------------------

Configure in: /var/openstorefront/config/openstorefront.properties

( **Property** -description ( **Default** ))

-  **errorticket.max**          -            Max amount of ticket to hold (culls the oldest records upon filling)                                                                                                       ( **5000** )
-  **trackingrecords.max.age.days**  -       Max age of tracking records                                                                                                                                                ( **365** )
-  **solr.server.url**     -                 URL to the SOLR instance to use. ; it should point to the appropriate collection.                                                                               ( **http://localhost:8983/solr/esa** )
-  **db.connectionpool.min**    -            DB min pool size                                                                                                                                                           ( **5** )
-  **db.connectionpool.max** -               DB max pool size                                                                                                                                                           ( **40** )
-  **db.user**             -                 Should match orientdb-server-config.xml                                                                                                                                    
-  **db.pw**            -                    Should match orientdb-server-config.xml
-  **external.host.url**   -                 Should point to the external host url (Eg. https://<host>/openstorefront) this is used in emails/external communication                                         ( **http://localhost:8080/openstorefront** )                                                                                                                                    
-  **job.working.state.override.minutes** -  Max job running time. Use for Integrations. To determine if a job got stuck.                                                                                               ( **30** )
-  **message.archive.days**        -         User message max age of archives                                                                                                                                           ( **30** )
-  **message.queue.minmintues**    -         User message queue time or the time the message waits before sending.                                                                                                      ( **10** )
-  **message.maxretires**    -               Max times the user message will try to send if unable to deliver.                                                                                                          ( **5** )
-  **message.recentchanges.days**   -        Time between "recent changes" messages from being sent.                                                                                                                    ( **28** )
-  **app.title**   -                         Title of the application. Used in emails but, also other places.                                                                                                           ( **DI2E Storefront** )
-  **external.usermanager**   -              Specifies the manager that is used for external user management. The manager must be supported by the application. ( IniRealmManager or LdapUserManager)                   ( **IniRealmManager** )
-  **external.sync.activate**  -             Set to 'true' to run the sync                                                                                                                                              (**False**)
-  **dblog.on**        -                     Activates logging records to the database; Note: All log record are still logged in the server logs regardless of setting this. This just controls the database logging.   ( **false** )
-  **dblog.maxrecords**     -                Maximum database records to store                                                                                                                                          ( **50000** )
-  **dblog.logSecurityFilter**  -            Log security API audit records; Note: setting this to true can cause noise when using the application log viewer.                                                          ( **False** )
-  **jirafeedback.show** - Allows users to provide jira feedback (True/False) ( **True** )
-  **filehistory.max.days** - Sets the max days to keep file history ( **180** )
-  **notification.max.days** - Set the max days to keep notification messages ( **7** )
-  **report.lifetime** - Set the lifetime of a report (how long will this report be in the system?) ( **180** )
-  **feedback.email** - Email address to send feedback to
-  **ui.idletimeout.minutes** - Set to a value > 1 to have the UI popup a idle warning about their session (Default is the application tries to keep the session alive.)
-  **ui.idlegraceperiod.minutes** -Set this to configure the grace period for the idle timeout. After the message appears.
-  **system.archive.maxprocessminutes** -Max time for system archive process without making progress (**60**)
-  **websockets.enabled** - Enables the use of websockets for server notifications ( **False** )
-  **userreview.autoapprove** - Allows user reviews, Questions, and answers submitted by users to be automatically approved, otherwise an administrator must approve each update ( **True** )
-  **role.admin** -Set this before strarting the application the first time to set the name of the Admin Role
-  **test.email** -Set to run container tests that require email
-  **system.archive.maxprocessminutes** -Set the max time for running archive process; this used to clean up stuck working archives ( **60** )

# 3. Database Management
-----

The application handles all database interaction transparently, so
direct database access and manipulation is not needed.  

See the following for information on outside control (should rarely be
needed/used).

## 3.1 Refreshing the Database
-----------------------

**CAUTION:** This will wipe out all data in the application. Data, such
as User profiles, cannot be recovered. Component user data can be
preserved by performing an export from the component admin tool.

Make a backup by copying all of the files in the /var/openstorefront/db
directory or use the following console tools steps:

1.  Stop the Tomcat server  (e.g. service tomcat stop)

2.  Remove the folder /var/openstorefront/db
    (rm -rf /var/openstorefront/db)

3.  Start the tomcat server

When the application loads it will create a new database and populate
the data from whatever is currently in the import folders (lookups only; attributes, component, articles will need to be manually
trigger or uploaded via the Admin Tools UI).

The initial load of the application may take a few minutes. If the
import directories are empty, the application will load default lookup
files that are packaged with the application.

## 3.2 Installing Database Console
----------------------------

**CAUTION:** Viewing (Querying) information is fine; however, use
extreme caution when modifying any records as all logic is handled by
the application.

1.  Download Orient DB (Currently using the 2.1.x series) at
    [OrientDB.org](http://www.orientechnologies.com/download/)

2.  Extract the archive

3.  Run the console ./bin/console.sh

4.  Connect to the DB:

connect remote: localhost/openstorefront (user) (password) (see the
    /var/openstorefront/config/openstorefront.properties for
    connection information)

The database supports an SQL like interface and then adds other
functionality on top.

-   See [Orient DB Backup](http://www.orientechnologies.com/docs/last/orientdb.wiki/Backup-and-Restore.html) for
    information about backup

-   See [Orient DB Export/Import](http://www.orientechnologies.com/docs/last/orientdb.wiki/Export-and-Import.html) for
    export and imports.

## 3.3 Installing Database Studio
----------------------------

**NOTE** Orient DB includes a web application for viewing the database
visually, instead of viewing everything from the console. Once installed,
Orient DB Studio will run with the database itself once OpenStoreFront
is running, and will not require anything to be run locally

**CAUTION:** Viewing (Querying) information is fine; however, use
extreme caution when modifying any records as all logic is handled by
the application.

1.  Download Orient DB (Currently using the 2.1.x series) at
    [OrientDB.org](http://www.orientechnologies.com/download/)

  1. If you already downloaded Orient DB in section 3.2 above,
    you may simply reuse that download.

2.  Extract the archive

3.  Locate the Studio plugin: ./plugins/studio-2.1.zip

4.  Copy plugin to OpenStoreFront database on server:
    /var/openstorefront/db/plugins

  1. Copy entire .zip file; do not extract.

5. Start or Restart OpenStoreFront

  1. Plugin will automatically be installed when the database service
        is initialized.

6. Access Orient DB Studio: <http://localhost:2480/studio/index.html>

  1. Change 'localhost' to the appropriate domain name or IP address if OpenStoreFront is not running locally
  2. Ensure 'openstorefront' is selected as the database in the dropdown.
  3. Login using the credentials located in the configuration file: /var/openstorefront/config/openstorefront.properties

The database supports an SQL like interface and then adds other
functionality on top.

-   See [Orient DB Studio](http://orientdb.com/docs/2.1.x/Home-page.html) for
    more information about Studio
