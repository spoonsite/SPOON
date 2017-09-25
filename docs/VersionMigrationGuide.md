# Version Migration Guide

In general, you should always upgrade one version at a time in order. (IE. going from 2.2 to 2.4 then 2.2 to 2.3 then to 2.4)  That way data migrations will occur in the proper order.  If you start from the lastest version and have no data then migration is not needed.

## Note: 2.4.3

This includes the database upgrade.  Note: rollback to pervious storefront version will require restore from a backup proir to 2.4.3 for existing data to work for that version.

**2.4.2** Must be deployed before 2.4.3 if you have existing data.

## Note: 2.4.2

Includes a database export and import in preparation for 2.4.3 upgrade of DB.  If you have an existing database install 2.4.2 before 2.4.3.

1. Shutdown app server
2. Backup /var/openstorefront/db directory
3. install/deploy 2.4.2
4. Start app server

You need to wait for 5-20 minutes (Depending on size of DB) for the update to occur. At this time the application will be unavailable. 

To manually export/import:

1. install orient 2.1.25 to system to use console tool
a.) Shutdown app server

From Orient

> cd bin
> ./console.sh (or bin/console.bat under Windows)
orientdb> CONNECT plocal:/var/openstorefront/db/openstorefront <DBuser> <db password
orientdb> EXPORT DATABASE /temp/mydb.json.gz
orientdb> DISCONNECT
  (orientdb> CREATE DATABASE plocal:/var/openstorefront/db/openstorefront only do this if you have move the old one out of the way)
orientdb> IMPORT DATABASE /temp/mydb.json.gz



## Upgrading from 2.3 to 2.4
-------------

**Note:** As part of the upgrade, Metadata will be automatically converted to Attributes.  This may take a while, if there is a lot of Metadata associated with the entries.  The server won't be avaliable until the migration is complete.  

Pre- Deployment 
------------------------------------------------------------------------------------------------------------------------------- 
1. Edit /var/openstorefront/config/shiro.ini 
under [url] section: 

Confirm line "/images" is 

/images/* = anon 

If not then update it. 

Then restart server if it's running to apply changes.



## Upgrading from 2.2 to 2.3
---------

DI2E environments that use open am should follow JIRA ticket STORE-1243.

1. **Update Security**  - If you haven't done any customization then the easiest
upgrade path is to just remove the the existing shiro.ini.

	a) Delete /var/openstorefront/config/shiro.ini
	
	b) On next server restart the application will pull the default
	
	Keep in mind this is for environments that use the built in user management rather then an external user management.
	The default shiro config is set for the built in user management.

2. **Update Database** - 2.3 includes a update to the database.  

	a) Make sure tomcat is shutdown

	b) Make a backup of existing db 

			i) Copy /var/openstorefront/db directory to backup location 
		
	c) Delete all /var/openstorefront/db/databases/openstorefront/openstorefront.*.wal files (just the WAL files there maybe 1 or more) (This appears to be optional)




