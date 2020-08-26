CLASSES_DIR=build/classes
TOMCAT_DIR=build/apache-tomcat-8.5.55
TOMCAT_PATH=/Users/akhilesh/softwares/apache-tomcat-8.5.55
TOMCAT_WEBAPPS=$TOMCAT_DIR/webapps
WAR_FILE=build/war/ROOT.war


clean_classes_dir() {
	rm -rf $CLASSES_DIR
	print_message "Creating Dir: $CLASSES_DIR"
	mkdir -p $CLASSES_DIR
}


ant_build() {
	print_message "Building Project: ant -f build.xml"
	ant -f build.xml
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
        	exit $exit_status
	fi
	print_message "Project Build SUCCESSFUL"

}

run_server() {
	rm -rf TOMCAT_DIR
	cp -r $TOMCAT_PATH ./build/
	rm -rf $TOMCAT_WEBAPPS/ROOT.war $TOMCAT_WEBAPPS/ROOT
	cp $WAR_FILE $TOMCAT_WEBAPPS
	print_message "STARTING TOMCAT SERVER"
	sh $TOMCAT_DIR/bin/catalina.sh run
}

print_message() {
	echo "--------------------------------------------------------------------------------------------"
	echo "-------------$@----------------"
	echo "--------------------------------------------------------------------------------------------"
}

clean_classes_dir
ant_build
run_server


