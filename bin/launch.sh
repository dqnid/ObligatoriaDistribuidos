#!/bin/bash

./stop.sh
for dir in ../tomcat/*/
do
	chmod +x $dir/bin/*
	rm ../log/*
	rm -rf $dir/ssdd/
	cp ./ssdd.war $dir/webapps/ssdd.war

	$dir/bin/startup.sh
done
if test $# -gt 1; then
	if test $1 == "main"; then
		java -jar ssdd_launcher.jar ../ssdd.cfg ../../log/
		mergeLogs.sh
	fi
fi
