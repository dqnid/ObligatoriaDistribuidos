#!/bin/bash

for dir in ../tomcat/*/
do
	chmod +x $dir/bin/*
	rm ../log/*
	rm -rf $dir/ssdd/
	cp ./ssdd.war $dir/webapps/ssdd.war

	$dir/bin/startup.sh
done
java -jar ssdd_launcher.jar ../ssdd.cfg ../log/
mergeLogs.sh
