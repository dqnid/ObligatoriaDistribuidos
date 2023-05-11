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
		String msg;
		String currentThreadLogPath = this.logFolder+"/thread"+this.nodeId + ".log";
		if (nodeIpList == null) {
			msg = "Error: durante la obtención de la lista de IPs.\nNodo \"+ this.nodeId +\" finalizado con errores";
			FileHelper.log(currentThreadLogPath, msg, true);
		} else {
			msg = "Llamando a la función de arranque del proceso " + this.nodeId + "...";
			FileHelper.log(currentThreadLogPath, msg, true);
			String response = ClientHelper.startProcess(this.nodeIp, nodeIpList, this.nodeId, this.logFolder, this.ntpServer);
			if (response == "failed" || response == null) {
				msg = "Error: en el proceso 'start' del nodo\nNodo \"+ this.nodeId +\" finalizado con errores";
			} else {
				FileHelper.logTimesFromString(""+this.logFolder + "/" + this.nodeId + "_FinalAdjusted.log", response);
				msg = "Nodo "+ this.nodeId +" finalizado correctamente";
			}
			FileHelper.log(currentThreadLogPath, msg, true);
			FileHelper.log(logFolder+"/ntp_delay_node" + this.nodeId + ".log", "" + ClientHelper.requestDelay(this.nodeIp), false);
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
		String logMsg;
		String mainThreadLogPath;
		int id;

		if (args.length != 2) {
			logMsg = "Asumiendo ubicación del fichero de cfg y la carpeta de logs\\ncfg: ' ./ssdd.cfg'\\nlog: ' ./log'";
			cfgFile = "./ssdd.cfg";
			logFolder = "./log";
		} else {
			cfgFile = args[0];
			logFolder = args[1];
			logMsg = "Parámetros obtenidos de argumentos:\n- Fichero de configuración: " + cfgFile + "\n- Directorio de logs: " + logFolder;
		}
		
		mainThreadLogPath = logFolder +"/mainThread" + ".log";
		FileHelper.log(logFolder+"/mainThread" + ".log", logMsg, true);
		
		ArrayList<String> ipList = FileHelper.getListIP(cfgFile);
		if (ipList == null) {
			logMsg = "Error: Fichero de configuración inaccesible.";
			FileHelper.log(mainThreadLogPath, logMsg, true);
			System.exit(-2);
		}
		
		logMsg = "Fichero de configuración leído correctamente.";
		FileHelper.log(mainThreadLogPath, logMsg, true);
			
		ntpServer = FileHelper.getNTPServer(cfgFile);
		if (!FileHelper.folderExists(logFolder) || ntpServer == null) {
			logMsg = "Error: Directorio de logs inaccesible.";
			FileHelper.log(mainThreadLogPath, logMsg, true);
			System.exit(-3);
		}
		logMsg = "Directorio de logs localizado.";
		FileHelper.log(mainThreadLogPath, logMsg, true);
		
		id = 0;
		for (String current_ip : ipList) {
			Launcher current_launcher = new Launcher(current_ip, id+1, cfgFile, logFolder, ntpServer);
			current_launcher.start();
			id++;
		}		
	}
}