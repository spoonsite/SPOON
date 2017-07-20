#!/bin/sh

for i in "$@"
do
case $i in
    --to-version=*)
   
    $CATALINA_HOME/bin/catalina.sh stop 30
    
    URL=$STOREFRONT_WAR_URL
    curl -fSL "${URL/$STOREFRONT_VERSION/${i#*=}}" -o $CATALINA_HOME/webapps/${i#*=}.war 
    
    if [ -f "$CATALINA_HOME/webapps/${i#*=}.war" ]; then
        
        rm -rf $CATALINA_HOME/webapps/ROOT.war $CATALINA_HOME/webapps/ROOT/
        mv $CATALINA_HOME/webapps/${i#*=}.war $CATALINA_HOME/webapps/ROOT.war
    fi
   
   $CATALINA_HOME/bin/catalina.sh start
    
    shift
    ;;
    *)
   ;;
esac
done

echo -e '
  Program has completed.
  If you upgraded, please wait a few moments for the new version to initialize.
  You may exit the terminal by entering "exit" (without quotes) and pressing Enter. 
  '
