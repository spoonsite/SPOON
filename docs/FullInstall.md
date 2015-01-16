#High Level instruction for a fresh install
Pre-install: setup ESA or a Solr instance and make sure it's running

	1. Setup VM 
	2. Install Java jdk 1.8 
	3. Install Tomcat 7 
	4. Integrate Open AM Agent 
	4. Deploy Application 
	5. Configure Application 
	6. Restart Tomcat (To pickup configuration changes) 
	7. Import Data 


##Suggested VM 

	CentOS
	CPUs: 4
	RAM: 8GB 
	DISK: 40GB

##Platform Dependancies

	Java 8
	Tomcat 7 >v50

##External Dependancies
Open AM (Optional)
ESA or Solr (Base solr will require some changes to the schema.xml to make sure all field are available)

##System Setup
*Unless otherwise noted run as sudo
*nano is a text edit another editor may be used.  

###Install java

	1.  Download JDKs from Oracle
	2.  Move the downloads to the server (home directory)
	3.  Sudo su (run as root)
	4.  Extract and move folders
	
		a.  mkdir /usr/java	
		c.  tar -xvf jdk-8u25-linux-x64.tar.gz
		e.  mv jdk1.8.0_25 /usr/java/
		f.  chmod 755 -R /usr/java
	
	5.  Create Links
	
		b.  ln -s /usr/java/jdk1.8.0_25 /usr/java/jdk8
		c.  ln -s /usr/java/jdk1.8.0_25 /usr/java/latest
	
	6.  Setup Environment Vars
	
		(see http://www.cyberciti.biz/faq/linux-unix-set-java_home-path-variable/")
	
		a.  nano /etc/profile
		b.  Add to the bottom:
			#Java Path
			export PATH=$PATH:/usr/java/latest/bin
			export JAVA_HOME=/usr/java/latest
		c.  Save/Exit then  source /etc/profile
		d.  nano /etc/bash.bashrc
		e.  Add to the bottom:
			#Java Path
			export PATH=$PATH:/usr/java/latest/bin
			export JAVA_HOME=/usr/java/latest
		f.   Save/Exit then  source /etc/bash.bashrc
		g.  Confirm “java –version” it should run

###Install Tomcat Public Package

	1.	Download and copy to home directory
	2.	tar -xvf apache-tomcat-7.0.55.tar.gz
	3.	mv apache-tomcat-7.0.55 /usr/local/tomcat
	4.	ln -s /usr/local/tomcat/apache-tomcat-7.0.55 /usr/local/tomcat/latest
	(use ln –nsf (target) (link) to repoint)
	
	5.	nano /usr/local/tomcat/latest/bin/setenv.sh
	Add line :
	CATALINA_OPTS=-Xmx1024m
	
	Note: memory settings depend on server config. 
	Minimum recommended memory is 1GB however, it may be able to run on less.  
	The amount of memory affects the amount of concurrent user the server can support.
	
		VM RAM: 
		2GB -> -Xmx1024m
		4GB -> -Xmx2048m  
		8GB -> -Xmx6144m  (Make sure to use 64bit VM)
	
	
	In the conf/server.xml (Optional)
	Add http compression on
	compression="on" compressableMimeType="text/html,text/xml,text/plain,application/json,text/css"
	
	6. Configure Service
	Setup tomcat as a service (http://www.davidghedini.com/pg/entry/install_tomcat_7_on_centos")
		a.	nano /etc/init.d/tomcat
			#!/bin/bash
			# description: Tomcat Start Stop Restart
			# processname: tomcat
			# chkconfig: 234 20 80
			#JAVA_HOME=/usr/java/jdk8
			#export JAVA_HOME
			#PATH=$JAVA_HOME/bin:$PATH
			#export PATH
			CATALINA_HOME=/usr/local/tomcat/latest 
			case $1 in
			start)
			sh $CATALINA_HOME/bin/startup.sh
			;; 
			stop)   
			sh $CATALINA_HOME/bin/shutdown.sh
			;; 
			restart)
			sh $CATALINA_HOME/bin/shutdown.sh
			sh $CATALINA_HOME/bin/startup.sh
			;; 
			esac    
			exit 0
		b.  chmod 755 /etc/init.d/tomcat
		c.  chkconfig --add tomcat 
		d.  chkconfig --level 234 tomcat on
	
	7. Open port iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
	 
###Install Tomcat Using jpackage
	http://www.jpackage.org/

	1. Follow the instruction about setup the repo for your system install (Eg. yum, apt...etc)
	2. Pull down tomcat7-7.0.54-2 from the version 6 repo.
	3. NOTE jpackage version has a mistake in the package.   The ecj jar library is very old and doesn't work with 	         jdk1.8.   However, this is easily corrected.   Just download 7.0.54 from apache.org and replace ecj-xxx.jar in tomcat/lib directory with ecj-xxx.jar from the 7.0.54 version.


###Server Control

service tomcat start
service tomcat stop
service tomcat restart

##Deploying application

Copy openstorefront.war to /usr/local/tomcat/latest/webapps

##Application Configuration
Configuration and data is stored in /var/openstorefront/  **(make sure user running the application has r/w permission for that directory)**  All directories are created upon application startup.

High level directory map under /var/openstorefront/

	config - holds all configurations files.  Defaults are created on initial startup.
	db - hold database files
	import -directory for placing files to be imported.
	perm - permanment storage location
	temp - temporary storage that application controls.  
		Note: most application temp storage defaults to the system temp storage location.  
		The temp directory here	holds information that needs to be persisted for long time period.

The main configuration file is:  /var/openstorefront/config/openstorefront.properties

On Intitial setup modify the following:

	1. change the url to point to esa/solr  "solr.server.url=http://localhost:15000/solr/esa" 

	2. For Open Am Integration:
	 a)
		#Security Header
		openam.url=<Url to open am something like http:/idam.server.com/openam
		#http:/.../openam/UI/Logout  (Full URL)
		logout.url=<Full url to the logout>
		#Change any header as needed.  The defaults likely will work out of the box.
		openam.header.username=sAMAccountName
		openam.header.firstname=givenname
		openam.header.lastname=sn
		openam.header.email=mail
		openam.header.group=memberOf
		openam.header.ldapguid=memberid
		openam.header.organization=
		openam.header.admingroupname=STORE-Admin
	
	b) Edit shiro.ini under the config directory
		under [main]
			uncomment (remove #)
			headerRealm = edu.usu.sdl.openstorefront.security.HeaderRealm
			securityManager.realms = $headerRealm
		under [users]
			comment out
			#admin = secret, administrator
			#user = user
		under [roles]
			comment out
			#admin = administrator

##Importing Data
Import Directory /var/openstorefront/import/article  
	place article files (html) here and the system will pick them up.
	
Import Directory /var/openstorefront/import/components
	component file (json) should be placed here to be picked up (see https://github.com/di2e/openstorefront/tree/master/server/components) for examples


##Logging Notes
Logs messages will show in the "catalina" log file.  /usr/local/tomcat/latest/logs

###Log Level Definitions
	SEVERE - Something didn't run correctly or as expected
	WARN -Bahavior may not be desired, but the system was able to continue
	INFO -System Admin messages
	FINE -Developer messages 
	FINER -Detailed Developer messages
	FINEST -Trace information
	
	
	
	
	


