#!/bin/bash

for dir in ../tomcat/*/
do
	chmod +x $dir/bin/*
	$dir/bin/shutdown.sh
done
