+++
title = "Administrator Guide"
description = ""
weight = 4
+++

This article gives further instructions on setting up and configuring the web application.

<!--more-->

## Related Documentation

[Architecture]({{< ref "Architecture.md" >}})

[Setup]({{< ref "Setup.md" >}})

[Developer Guide]({{< ref "../dev/DevelopersGuide.md" >}})

## Application Properties

Configure in: `/var/openstorefront/config/openstorefront.properties`

| Property                            | Description                                                             | Default                                                      |
|-------------------------------------|-------------------------------------------------------------------------|--------------------------------------------------------------|
| **Database**                        |                                                                         |                                                              |
| db.connectionpool.min               | DB min pool size                                                        | 5                                                            |
| db.connectionpool.max               | DB max pool size                                                        | 40                                                           |
| db.user                             | Should match orientdb-server-config.xml                                 | app                                                          |
| db.pw                               | Should match orientdb-server-config.xml                                 | aPpw0rd!                                                     |
| db.use.mongo                        | If the application should use MongoDB instead of OrientDB               | true                                                         |
| mongo.database                      | The name of the database in MongoDB                                     | storefront                                                   |
| errorticket.max                     | Max amount of ticket to hold (culls the oldest records upon filling)    | 5000                                                         |
| trackingrecords.max.age.days        | Max age of tracking records                                             | 365                                                          |
| external.usermanager                | Specifies manager for external user management (LdapUserManager)        | IniRealmManager                                              |
| external.sync.activate              | Set to true to run the sync                                             | false                                                        |
| search.server                       | The search server that the application uses (must use elasticsearch)    | elasticsearch                                                |
| elastic.server.host                 | The hostname of the machine hosting Elasticsearch                       | localhost                                                    |
| elastic.server.port                 | The port that elasticseach is using                                     | 9200                                                         |
|                                     |                                                                         |                                                              |
| **Security Header**                 | **Not in use for SPOON**                                                |                                                              |
| openam.url                          | http:/…/openam (Full URL to open am instance)                           |                                                              |
| logout.url                          | http:/…/openam/UI/Logout (Full URL to logout)                           |                                                              |
| openam.header.username              | HTTP Header for Username                                                | sAMAccountName                                               |
| openam.header.firstname             | HTTP Header for Firstname                                               | givenname                                                    |
| openam.header.lastname              | HTTP Header for Lastname                                                | sn                                                           |
| openam.header.email                 | HTTP Header for email                                                   | mail                                                         |
| openam.header.phone                 | HTTP Header for group                                                   | telephonenumber                                              |
| openam.header.group                 | HTTP Header for ldapguid                                                | memberOf                                                     |
| openam.header.ldapguid              | HTTP Header for organization                                            | memberid                                                     |
| openam.header.organization          |                                                                         | company                                                      |
| openam.header.admingroupname        | HTTP Header for Admin Group Name \*Handles multiple values              | STOREFRONT-Admin                                             |
|                                     |                                                                         |                                                              |
| **Security**                        |                                                                         |                                                              |
| role.admin                          | Name of the Admin Role                                                  | STOREFRONT-Admin                                             |
|                                     |                                                                         |                                                              |
| **Dev Tools Login**                 | **Not in use for SPOON**                                                |                                                              |
| tools.login.user                    |                                                                         |                                                              |
| tools.login.pw                      |                                                                         |                                                              |
|                                     |                                                                         |                                                              |
| **Jira Integration**                | **Deprecated**                                                          |                                                              |
| jira.connectionpool.size            |                                                                         | 20                                                           |
| jira.connection.wait.seconds        |                                                                         | 60                                                           |
| jira.server.url                     |                                                                         | [https://jira.di2e.net](https://jira.di2e.net)               |
|                                     |                                                                         |                                                              |
| **Job management**                  |                                                                         |                                                              |
| job.working.state.override.minutes  | Max job running time, used for Integrations                             | 30                                                           |
| task.complete.expireminutes         |                                                                         | 5                                                            |
| task.error.expireminutes            |                                                                         | 4320                                                         |
| report.lifetime                     | How long will this report be in the system?                             | 180                                                          |
|                                     |                                                                         |                                                              |
| **Email Support**                   |                                                                         |                                                              |
| mail.smtp.url                       | Login Credentials for Integrations (currently just for jira)            | localhost                                                    |
| mail.server.user                    | Login Credentials for mail server                                       |                                                              |
| mail.server.pw                      | Login Credentials for mail server                                       |                                                              |
| mail.smtp.port                      | Mail Port (25 common)                                                   |                                                              |
| mail.use.ssl                        | Set to true if server requires it                                       |                                                              |
| mail.use.tls                        | Set to true if server requires it                                       |                                                              |
| mail.from.name                      | From Name                                                               | Storefront Notification                                      |
| mail.from.address                   | From Email Address                                                      | donotreply@storefront.net                                    |
| mail.reply.name                     | Reply name (usually display at the bottom the message)                  |                                                              |
| mail.reply.address                  | Reply email (usually display at the bottom the message)                 |                                                              |
| mail.attach.file                    | Set to true or false indicating if emails should contain attached files | 0                                                            |
| message.archive.days                | User message max age of archives                                        | 30                                                           |
| message.queue.minmintues            | User message queue time or the time the message waits before sending    | 10                                                           |
| message.maxretires                  | Max times the user message will try to send if unable to deliver        | 5                                                            |
| message.recentchanges.days          | Time between “recent changes” messages from being sent                  | 28                                                           |
|                                     |                                                                         |                                                              |
| **General**                         |                                                                         |                                                              |
| app.title                           | Title of the application. Used in emails but, also other places.        | Storefront                                                   |
| websockets.enabled                  | Enables the use of websockets for server notifications                  | false                                                        |
| userreview.autoapprove              | Allows user submitted info to be automatically approved (reviews, ect.) | true                                                         |
| max.post.size                       | Maximum file size for files being saved to the system (in MB)           | 2000                                                         |
|                                     |                                                                         |                                                              |
| **LDAP Manager**                    | **Not in use for SPOON**                                                |                                                              |
| ldapmanager.url                     | Full URL to the LDAP                                                    |                                                              |
| ldapmanager.userDnTemplate          | uid={0},ou=users,dc=mycompany,dc=com; Reserved, not currently used      |                                                              |
| ldapmanager.authenticationMechanism | NONE, SIMPLE, DIGEST-MD5, etc.                                          | SIMPLE                                                       |
| ldapmanager.security.sasl.realm     | May be needed for SASL authentication                                   |                                                              |
| ldapmanager.binddn                  | The LDAP user to use in the connection (Full DN name)                   |                                                              |
| ldapmanager.pw                      |                                                                         |                                                              |
| ldapmanager.connectionTimeout       |                                                                         | 15000                                                        |
| ldapmanager.contextRoot             | Root to directory to search                                             |                                                              |
| ldapmanager.attribute.username      | Attribute to map to username                                            | sAMAccountName                                               |
| ldapmanager.attribute.email         | Attribute to map to email                                               | mail                                                         |
| ldapmanager.attribute.phone         | Attribute to map to phone number                                        | telephonenumber                                              |
| ldapmanager.attribute.fullname      | Attribute to map to fullname                                            | name                                                         |
| ldapmanager.attribute.organization  | Attribute to map to organization                                        | company                                                      |
| ldapmanager.attribute.guid          | Attribute to map to guid                                                | objectGUID                                                   |
|                                     |                                                                         |                                                              |
| **Help**                            |                                                                         |                                                              |
| help.url                            | The URL that hosts this documentation                                   | [https://spoonsite.github.io/](https://spoonsite.github.io/) |

## Configuration

This section contains information on how to configure LDAP and OpenAM for SPOON. These features are currently inactive
on SPOON, but could be activated in the future.

### LDAP Configuration

{{% notice warning %}}
LDAP is currently not in use on SPOON.
{{% /notice %}}

Configure in `/var/openstorefront/config/shiro.ini`

```ini
[main]

ldapRealm = org.apache.shiro.realm.ldap.JndiLdapRealm

ldapRealm.userDnTemplate = uid={0},ou=users,dc=mycompany,dc=com

ldapRealm.contextFactory.url = ldap://ldapHost:389

ldapRealm.contextFactory.authenticationMechanism = DIGEST-MD5

ldapRealm.contextFactory.environment[some.obscure.jndi.key] = some
value
```

#### Database

See
[Configure JDBC Realm](http://stackoverflow.com/questions/17441019/how-to-configure-jdbcrealm-to-obtain-its-datasource-from-jndi)
for how to configure JDBCRealm to obtain its DataSource from JNDI.

```ini
[main]

dataSource = org.apache.shiro.jndi.JndiObjectFactory

dataSource.resourceName = java://app/jdbc/myDataSource

jdbcRealm = org.apache.shiro.realm.jdbc.JdbcRealm

jdbcRealm.permissionsLookupEnabled = true

jdbcRealm.dataSource = $dataSource

# you can customize the authenticationQuery, userRolesQuery and permissionsQuery,  if needed.

securityManager.realms = $realm
```

#### OPENAM (Request Header)

{{% notice warning %}}
OpenAM is currently not in use on SPOON.
{{% /notice %}}

OpenAM configuration

```ini
[main]

#Also, remember to comment out the users and roles to remove the INIRealm

headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm

securityManager.realms = $headerRealm
```

#### Integration with OpenAM

1. Adjust the open am agent filter

    Change:

    ```xml
        <filter-mapping>
            <filter-name>Agent</filter-name>
            <url-pattern>/*</url-pattern>
            <dispatcher>REQUEST</dispatcher>
            <dispatcher>INCLUDE</dispatcher>
            <dispatcher>FORWARD</dispatcher>
            <dispatcher>ERROR</dispatcher>
        </filter-mapping>
    ```

    To

    ```xml
        <filter-mapping>
            <filter-name>Agent</filter-name>
            <url-pattern>/*</url-pattern>
            <dispatcher>REQUEST</dispatcher>
            <dispatcher>ERROR</dispatcher>
        </filter-mapping>
    ```

1. Update system properties configurations

    See Security Header in [Application Properties](#application-properties)

## User Types

The user types are defined as based on the roles they belong to.

If using the built-in security then the system will create default roles for default users, admins and evaluators. It
will also create a default admin user: (admin / Secret1@)

{{% notice warning %}}
You should change the admin password after login in.
{{% /notice %}}

Pre-defined groups:

**Default** - Allows Searching, tagging, watching, and submitting entries

**Guest** -Permissions for guest user. (Only applies if security is setup for that)

**Admin** - All administration permissions. There maybe some optional features that the admin doesn't by default have permission.

**Librarian** -Data management permissions.

**Evaluators** - Allows user to edit unpublished evaluations

An admin can define new groups as needed.

{{% notice note %}}
All users are part of the Default group.
{{% /notice %}}

## Database Management

The application handles all database interaction transparently, so direct database access and manipulation is not needed.

### Refreshing the Database

{{% notice warning %}}
This will wipe out all data in the application. Data, such as User profiles, cannot be recovered. Component user data can
be preserved by performing an export from the component admin tool.
{{% /notice %}}

Make a backup by copying all of the files in the /var/openstorefront/db directory or use the following console tools steps:

1. Stop the Tomcat server (e.g. service tomcat stop)

1. Remove the folder /var/openstorefront/db

    ```sh
    rm -rf /var/openstorefront/db
    ```

1. Start the tomcat server

When the application loads it will create a new database and populate the data from whatever is currently in the import
folders (lookups only; attributes, component, articles will need to be manually trigger or uploaded via the Admin Tools UI).

The initial load of the application may take a few minutes. If the import directories are empty, the application will load
default lookup files that are packaged with the application.

## Troubleshooting

If elasticsearch detects low disk space it will change into Read-Only mode. At which point the index records can’t be changed
which cause with searching and updating records.

To resolve:

1. Correct the disk space issue

1. Force Elasticsearch out of read-only mode (this should only be done when the disk space issue cannot be resolved)

    ```sh
    curl -X PUT localhost:9200/_settings -H "Content-Type: application/json" -d '
    {
        "index": {
            "blocks": {
                "read_only_allow_delete": "false"
            }
        }
    }'
    ```

    Elasticsearch should respond with:

    ```json
    {
        "acknowledged": true
    }
    ```

See [https://discuss.elastic.co/t/forbidden-12-index-read-only-allow-delete-api/110282/4](https://discuss.elastic.co/t/forbidden-12-index-read-only-allow-delete-api/110282/4)
