#!/bin/sh
su - $ES_NAME -c "$ES_HOME/bin/$ES_NAME -d"
$CATALINA_HOME/bin/catalina.sh start
tail -f $CATALINA_HOME/logs/catalina.out 
