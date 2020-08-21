TOMCAT_DIR=C:/Users/archu/Downloads/apache-tomcat-8.5.57-windows-x64/apache-tomcat-8.5.57
CURLY_OCTO_DIR=$TOMCAT_DIR/webapps/curly-octo-system
TOMCAT_WEBAPPS=$TOMCAT_DIR/webapps/curly-octo-system.war
WAR_FILE=build/war/ROOT.war

ant -f build.xml

exit_status=$?
if [ $exit_status -ne 0 ]
then
	exit $exit_status
fi

echo $TOMCAT_WEBAPPS

rm -rf $CURLY_OCTO_DIR $TOMCAT_WEBAPPS
cp $WAR_FILE $TOMCAT_WEBAPPS
sh $TOMCAT_DIR/bin/catalina.sh run