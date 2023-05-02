package ssdd;

import java.util.ArrayList;

import persistence.FileHelper;
import util.ClientHelper;

public class Launcher extends Thread{
	String cfgFile;
	String nodeIp;
	String logFolder;
	String ntpServer;
	int nodeId;
	
	public Launcher(String nodeIp, int nodeId, String cfgFile,String logFolder,String ntpServer) {
		this.nodeIp = nodeIp;
		this.nodeId = nodeId;
		this.logFolder = logFolder;
		this.cfgFile = cfgFile;
		this.ntpServer = ntpServer;
	}
	
	/*
	 * El arranque del servidor se hace en hilos (6 en total, 2 por máquina) 
	 * Dependencias (acoplamiento):
	 * - FileHelper para obtener el listado de IPs y escribir el log ajustado que ha devuelto el servidor
	 * - ClientHelper para lanzar la petición GET al servidor.
	 */
	public void run() {
		String nodeIpList = FileHelper.getFormatedIPList(cfgFile,nodeIp);
		if (nodeIpList == null) {
			System.out.println("Error: durante la obtención de la lista de IPs.");
			System.out.println("Nodo "+ this.nodeId +" finalizado con errores");
		} else {
			System.out.println("Llamando a la función de arranque del proceso " + this.nodeId + "...");
			String response = ClientHelper.startProcess(this.nodeIp, nodeIpList, this.nodeId, this.logFolder, this.ntpServer);
			if (response == "failed" || response == null) {
				System.out.println("Error: en el proceso 'start' del nodo");
				System.out.println("Nodo "+ this.nodeId +" finalizado con errores");
			} else {
				FileHelper.logFromString(""+this.logFolder + "/" + this.nodeId + "_FinalAdjusted.log", response);
				System.out.println("Nodo "+ this.nodeId +" finalizado correctamente");
			}
		}
	}
	
	/*
	 * Función principal
	 * Leemos ficheros de configuración y lanzamos hilos para el arranque simultáneo de los nodos
	 * @param cfgFile : String, logFolder : String
	 * */
	public static void main(String[] args) {
		String cfgFile;
		String logFolder;
		String ntpServer;
		int id;

		if (args.length != 2) {
			System.out.println("Asumiendo ubicación del fichero de cfg y la carpeta de logs\ncfg: ' ./ssdd.cfg'\nlog: ' ./log'");
			cfgFile = "./ssdd.cfg";
			logFolder = "./log";
		} else {
			cfgFile = args[0];
			logFolder = args[1];
		}
		
		ArrayList<String> ipList = FileHelper.getListIP(cfgFile);
		if (ipList == null) {
			System.out.println("Error: Fichero de configuración inaccesible.");
			System.exit(-2);
		} else {
			System.out.println("Fichero de configuración leído correctamente.");
		}
		ntpServer = FileHelper.getNTPServer(cfgFile);
		if (!FileHelper.folderExists(logFolder) || ntpServer == null) {
			System.out.println("Error: Directorio de logs inaccesible.");
			System.exit(-3);
		} else {
			System.out.println("Directorio de logs localizado.");
		}
		
		id = 0;
		for (String current_ip : ipList) {
			Launcher current_launcher = new Launcher(current_ip, id, cfgFile, logFolder, ntpServer);
			current_launcher.start();
			id++;
		}		
	}
}