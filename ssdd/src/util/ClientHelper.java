package util;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import java.util.concurrent.Semaphore;

/*
 * Helper class to manage HTTP requests
 * */
public class ClientHelper extends Thread{
	String target_ip;
	int t, p;
	static Semaphore sem;
	
	public ClientHelper(String target_ip, int t, int p)
	{
		this.target_ip = target_ip;
		this.t = t;
		this.p = p;
	}
	
	public void run()
	{
		try {
			ClientHelper.sem.acquire(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ target_ip + "/ssdd/").build();
		WebTarget target = client.target(uri);
		
		target.path("node").path("join").queryParam("t", this.t).queryParam("p", this.p).request(MediaType.TEXT_PLAIN).get(String.class);
		
		ClientHelper.sem.release(1);
	}
	
	/*
	 * Static methods
	 * returns 0 when successful
	 * returns < 0 when not
	 * Only request Entry runs on thread
	 * */
	public static int requestEntry(String[] target_ip_list, int t, int p)
	{
		ClientHelper.sem = new Semaphore(target_ip_list.length);
		for (String target_ip : target_ip_list) 
		{
			ClientHelper ch = new ClientHelper(target_ip,t,p);
			ch.run();	
		}
		
		try {
			ClientHelper.sem.acquire(target_ip_list.length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long[] requestNTP(String ntp_server, int n_evaluations) {
		Client client=ClientBuilder.newClient();
		URI uri;
		WebTarget target;
		
		long t0,t1,t2,t3;
		String[] server_times;
		long offset, delay;
		long[] bestPair = {0,0};
		
		uri=UriBuilder.fromUri("http://" + ntp_server + "/ssdd/").build();
		target = client.target(uri);
		for (int i = 0; i < n_evaluations; i++) {
			t0 = System.currentTimeMillis();
			server_times = target.path("node").path("getntp").request(MediaType.TEXT_PLAIN).get(String.class).split(";");
			t1 = Long.parseLong(server_times[0]);
			t2 = Long.parseLong(server_times[1]);
			t3 = System.currentTimeMillis();
			
			offset = ((t1-t0+t2-t3)/2);
			delay = (t1-t0+t3-t2);
			if (i == 0 || delay < bestPair[1]) {
				bestPair[0] = offset;
				bestPair[1] = delay;
			}
		}
		return bestPair;
	}
	
	/*
	 * El arranque del servidor se hace en hilos (6 en total, 2 por máquina) 
	 * Al servidor le enviamos:
	 * @params
	 * - nodeIpList : lista de ips formateada
	 * - nodeId : identificador
	 * - logFolder: directorio de logs propio
	 * - ntpServer: dirección del servidor ntp (con puerto si es necesario)
	 * El servidor devuelve:
	 * @return log : cadena con log ajustado
	 */
	public static String startProcess(String target_ip, String ip_list_nodes, int id, String logFolder, String ntpServer) {
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ target_ip + "/ssdd/").build();
		WebTarget target = client.target(uri);
				
		String respuesta = target.path("node").path("start").queryParam("ipList", ip_list_nodes).queryParam("id", id).queryParam("logFolder", logFolder).queryParam("ntpServer", ntpServer).request(MediaType.TEXT_PLAIN).get(String.class);
		return respuesta;
	}
}