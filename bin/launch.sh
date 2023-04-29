#!/bin/bash

./bin/stop.sh
for dir in ./tomcat/*/
do
	chmod +x $dir/bin/*
	now="$(date | awk '{print $4}')"
	mkdir ./log/old/$now
	mv ./log/*.log ./log/old/$now/
	rm -rf $dir/ssdd/
	cp ./bin/ssdd.war $dir/webapps/ssdd.war

	$dir/bin/startup.sh
done
if test $# -eq 1; then
	if test $1 == "main"; then
		java -jar ./bin/ssdd_launcher.jar ./ssdd.cfg ./log
		./bin/mergeLogs.sh
	fi
fi
