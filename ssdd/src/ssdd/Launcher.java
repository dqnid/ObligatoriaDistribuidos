package ssdd;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

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
	
	public void run() {
		/*
		 * Antes de contactar con el servidor (GET) separamos el identificador
		 * Al servidor le enviamos
		 * - ip_list_nodos : lista de ips formateada
		 * - current_ip : ip propia
		 * - id : identificador
		 */
		String nodeIpList = FileHelper.getFormatedIPList(cfgFile,nodeIp);
		
		String response = ClientHelper.startProcess(this.nodeIp, nodeIpList, this.nodeId, this.logFolder, this.ntpServer);
		
		FileHelper.logFromString(""+this.logFolder + "/" + this.nodeId + "_FinalAdjusted.log", response);
		System.out.println("Finalizado");
	}
	
	public static void main(String[] args) {
		String cfgFile;
		String logFolder;
		String ntpServer;
		int id;

		if (args.length != 2) {
			System.out.println("Asumiendo ubicación del fichero de cfg y la carpeta de logs");
			cfgFile = "/home/danih/Documents/Universidad/Segundo Cuatrimestre/Sistemas Distribuidos/Práctica/Entregas/ObligatoriaDistribuidos/ssdd.cfg";
			logFolder = "/home/danih/Documents/Universidad/Segundo Cuatrimestre/Sistemas Distribuidos/Práctica/Entregas/ObligatoriaDistribuidos/log";
		} else {
			cfgFile = args[0];
			logFolder = args[1];
		}
		
		ArrayList<String> ipList = FileHelper.getListIP(cfgFile);
		if (ipList == null) {
			System.out.println("Fichero de configuración inaccesible");
			System.exit(-2);
		}
		ntpServer = FileHelper.getNTPServer(cfgFile);
		/*if (FileHelper.folderExists(logFolder)) {
			System.out.println("Directorio de logs inaccesible");
			System.exit(-3);
		}*/
		
		/*
		 * Recorremos la lista de IPs
		 * */
		id = 0;
		for (String current_ip : ipList) {
			Launcher current_launcher = new Launcher(current_ip, id, cfgFile, logFolder, ntpServer);
			current_launcher.start();
			id++;
		}
	}
}