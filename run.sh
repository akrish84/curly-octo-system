TOMCAT_DIR=build/apache-tomcat-8.5.55
TOMCAT_PATH=/Users/akhilesh/softwares/apache-tomcat-8.5.55
TOMCAT_WEBAPPS=$TOMCAT_DIR/webapps
WAR_FILE=build/war/ROOT.war


ant -f build.xml
exit_status=$?
if [ $exit_status -ne 0 ]
then
	exit $exit_status
fi
rm -rf TOMCAT_DIR
cp -r $TOMCAT_PATH ./build/
rm -rf $TOMCAT_WEBAPPS/ROOT.war $TOMCAT_WEBAPPS/ROOT
cp $WAR_FILE $TOMCAT_WEBAPPS
sh $TOMCAT_DIR/bin/catalina.sh run

