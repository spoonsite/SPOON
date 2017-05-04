# Version Migration Guide


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




