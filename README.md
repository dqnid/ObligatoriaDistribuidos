# Práctica Obligatoria
* Sistemas Distribuidos, Universidad de Salamanca
* Curso 2022 - 2023

## Lanzamiento
Los scripts de lanzamiento deben ser lanzados desde el directorio raíz.
* `./bin/launcher.sh` : Lanzará los Tomcat que se encuentren en la carpeta `./tomcat`, copiando el fichero `./bin/ssdd.war` en su directorio de webapps. Antes del lanzamiento tratará de detenerlos. Admite un parámetro `main`, el cual producirá el lanzamiento del programa principal de java y todas las operaciones posteriores a este: unión de logs y lanzamiento del comprobador.
* `./bin/stop.sh` : detendrá todos los servidores Tomcat encontrados en el directorio `./tomcat`.
### Secuencia de lanzamiento:
1. `./bin/launch.sh` en los nodos secundarios.
2. `./bin/launch.sh main` en el nodo principal.

## TODO
* [x] Definir la estructura del fichero de configuración
	* [x] Usar el launcher para que envíe las IPs y los puertos
* [x] En start hacer que la petición de entrada a SC sea concurrente (quitar el for), con threads
* [x] Crear el proceso main 'launcher' que llame a los start de todos los procesos
	* [x] Usar hilos durante el lanzamiento
* [x] Generar logs
* [x] Escribir escript de lanzamiento para agilizar el proceso
* [x] Corregir error de interbloqueo durante los 2 primero ciclos
* [x] Crear endpoint NTP
	* [x] Ajustar fichero de configuración para admitir NTP
	* [x] Ajustar logs con offset NTP
	* [x] Log de delay NTP para comprobador
* [] Recuperar delays de forma automática con el script de arranque

## Detalles del sistema desarrollado
6 procesos repartidos en 3 nodos que gestionan una zona de exclusión mutua común.
- Algoritmo de Ricart y Agrawala
- Marcas temporales mediante Lamport
- Algoritmo de NTP

Ciclo de vida de un proceso:
1. Hace 'sleep' entre 0.3 y 0.5s -_esto simula un cálculo_-
2. Entra en sección crítica distribuida
3. Permanece en SC entre 0.1 y 0.3s

:repeat: Repite este proceso 100 veces

4. Termina de forma ordenada
### Logs
Cada vez que un proceso entra en la SC escribe en un fichero local:
_Px E tiempo_, dónde x es el id del proceso y tiempo es la salida de la función System.currentTimeMillis()
Cada vez que un proceso sale de la SC escribe en un fichero local:
_Px S tiempo_

## Dependencias de desarrollo
* Eclipse 2018
* Java
* Jersey - Jakarta
* Tomcat 9.0

## Autores
* Daniel Heras Quesada
* Guillermo Vicente González
