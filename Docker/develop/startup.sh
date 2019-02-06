#!/bin/sh

su - $ES_NAME -c "ES_JAVA_OPTS=\"-Xms512m -Xmx512m\" $ES_HOME/$ES_FILE/bin/$ES_NAME -d"
$CATALINA_HOME/bin/catalina.sh start
tail -f $CATALINA_HOME/logs/catalina.out
