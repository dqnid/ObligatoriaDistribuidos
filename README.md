# Práctica Obligatoria
* Sistemas Distribuidos, Universidad de Salamanca
* Curso 2022 - 2023

## TODO
- [x] Definir la estructura del fichero de configuración
	- [x] Usar el launcher para que envíe las IPs y los puertos
- [x] En start hacer que la petición de entrada a SC sea concurrente (quitar el for), con threads
- [  ] Hacer otro endpoint GET que se use para notificar
- [  ] Modificar el join para que guarde los elementos en la cola y responda 1 o 0
- [  ] Implementar una cola con las direcciones y puertos (en el mismo string) de los procesos solicitantes
- [x] Crear el proceso main 'launcher' que llame a los start de todos los procesos
- [  ] Lanzar de manera no cutre los procesos en un solo Tomcat
- [x] Generar logs

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
