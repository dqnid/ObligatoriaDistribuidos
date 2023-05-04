#!/bin/bash

# Debe ser lanzado desde la raíz
# Con lo siguiente se corrige pero falla si hay espacios en los nombres
# SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

printf '\e[38;2;0;119;182m\e[48;2;8;8;8mLimpiando logs...\e[0m\n'
now="$(date +%d%m%H%M%S)"
mkdir ./log/old/$now
mv ./log/*.log ./log/old/$now/

printf '\e[38;2;0;119;182m\e[48;2;8;8;8mIniciando proceso de arranque:\e[0m\n'
for dir in ./tomcat/*/
do
	printf '\e[38;2;0;119;182m\e[48;2;8;8;8mParando procesos previos...\e[0m\n'	
	chmod +x $dir/bin/*
	$dir/bin/shutdown.sh

	rm -rf $dir/ssdd/
	cp ./bin/ssdd.war $dir/webapps/ssdd.war

	printf '\e[38;2;0;119;182m\e[48;2;8;8;8mLanzando tomcat...\e[0m\n'	
	$dir/bin/startup.sh
done
if test $# -eq 1; then
	if test $1 == "main"; then
		printf '\e[38;2;0;119;182m\e[48;2;8;8;8mArrancando proceso principal...\e[0m\n'	
		java -jar ./bin/ssdd_launcher.jar ./ssdd.cfg ./log
		if test $? -ge 0; then
			printf '\e[38;2;0;119;182m\e[48;2;8;8;8mJuntando logs...\e[0m\n'	
			./bin/mergeLogs.sh
			printf '\e[38;2;82;183;136m\e[48;2;8;8;8m============\e[0m\n'	
			printf '\e[38;2;82;183;136m\e[48;2;8;8;8m¡Todo listo!\e[0m\n'	
			printf '\e[38;2;82;183;136m\e[48;2;8;8;8m============\e[0m\n'	
			printf '\e[38;2;255;224;102m\e[48;2;8;8;8mResultado final en ssdd.log\e[0m\n'	
			printf '\e[38;2;0;119;182m\e[48;2;8;8;8mEjecutando comprobador...\e[0m\n'	
			if [[ -f "./log/ntp_delay_node2.log" ]] && [[ -f "./log/ntp_delay_node4.log" ]]; then
				ntp2=$(cat ./log/ntp_delay_node2.log)
				ntp4=$(cat ./log/ntp_delay_node4.log)
				java -jar ./bin/ssdd_comprobador.jar ./ssdd.log $ntp2 $ntp4
			else
				java -jar ./bin/ssdd_comprobador.jar ./ssdd.log
			fi
		else
			printf '\e[38;2;230;57;70m\e[48;2;8;8;8mError detectado. Ejecución abortada\e[0m\n'	
		fi
	fi
fi
