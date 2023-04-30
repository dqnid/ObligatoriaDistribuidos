#!/bin/bash

mkdir ./log/old/$now
mv ./log/*.log ./log/old/$now/
for dir in ./tomcat/*/
do
	chmod +x $dir/bin/*
	now="$(date +%H%M%S)"
	rm -rf $dir/ssdd/
	cp ./bin/ssdd.war $dir/webapps/ssdd.war

	$dir/bin/startup.sh
done
if test $# -eq 1; then
	if test $1 == "main"; then
		java -jar ./bin/ssdd_launcher.jar ./ssdd.cfg ./log
	fi
fi
