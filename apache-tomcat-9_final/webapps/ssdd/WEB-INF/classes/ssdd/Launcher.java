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
	String dir_cfg = "/home/danih/.config/ssddcfg.cfg";
	String mi_ip;
	int mi_id;
	
	public Launcher(String mi_ip, int mi_id) {
		this.mi_ip = mi_ip;
		this.mi_id = mi_id;
	}
	
	public void run() {
		/*
		 * Antes de contactar con el servidor (GET) separamos el identificador
		 * Al servidor le enviamos
		 * - ip_list_nodos : lista de ips formateada
		 * - current_ip : ip propia
		 * - id : identificador
		 */
		String ip_list_nodos = FileHelper.getFormatedIPList(dir_cfg,mi_ip);
		
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ mi_ip + "/ssdd/").build();
		WebTarget target = client.target(uri);
		
		String respuesta = target.path("process").path("start").queryParam("ipList", ip_list_nodos).queryParam("id", mi_id).queryParam("myIp", mi_ip).request(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println(respuesta);
	}
	
	public static void main(String[] args) {
		String dir_cfg = "/home/danih/.config/ssddcfg.cfg";

		//Leemos la lista de ip del fichero de config, que luego tendremos que paremetrizar o algo
		ArrayList<String> ipList = FileHelper.getListIP(dir_cfg);
	
		/*
		 * Recorremos la lista de IPs
		 * */
		int id = 0;
		for (String current_ip : ipList) {
			Launcher current_launcher = new Launcher(current_ip, id);
			current_launcher.start();
			id++;
		}
	}
}