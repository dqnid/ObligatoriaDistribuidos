package ssdd;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import persistence.FileHelper;

public class Launcher extends Thread{
	String cfgFile = "/home/danih/.config/ssddcfg.cfg";
	String mi_ip;
	String logFolder;
	int mi_id;
	
	public Launcher(String mi_ip, int mi_id, String logFolder) {
		this.mi_ip = mi_ip;
		this.mi_id = mi_id;
		this.logFolder = logFolder;
	}
	
	public void run() {
		/*
		 * Antes de contactar con el servidor (GET) separamos el identificador
		 * Al servidor le enviamos
		 * - ip_list_nodos : lista de ips formateada
		 * - current_ip : ip propia
		 * - id : identificador
		 */
		String ip_list_nodos = FileHelper.getFormatedIPList(cfgFile,mi_ip);
		
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ mi_ip + "/ssdd/").build();
		WebTarget target = client.target(uri);
		
		String respuesta = target.path("node").path("start").queryParam("ipList", ip_list_nodos).queryParam("id", mi_id).queryParam("myIp", mi_ip).queryParam("logFolder", this.logFolder).request(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println(respuesta);
	}
	
	public static void main(String[] args) {
		String cfgFile;
		String logFolder;

		if (args.length != 2) {
			System.out.println("Asumiendo ubicación del fichero de cfg y la carpeta de logs");
			cfgFile = "/home/danih/.config/ssdd";
			logFolder = "/home/danih/.config/ssdd/ssdd.cfg";
		}
		cfgFile = args[0];
		logFolder = args[1];
		ArrayList<String> ipList = FileHelper.getListIP(cfgFile);
		if (ipList == null) {
			System.out.println("Fichero de configuración inaccesible");
			System.exit(-2);
		}
		/*if (FileHelper.folderExists(logFolder)) {
			System.out.println("Directorio de logs inaccesible");
			System.exit(-3);
		}*/
		/*
		 * Recorremos la lista de IPs
		 * */
		int id = 0;
		for (String current_ip : ipList) {
			Launcher current_launcher = new Launcher(current_ip, id, logFolder);
			current_launcher.start();
			id++;
		}
	}
}