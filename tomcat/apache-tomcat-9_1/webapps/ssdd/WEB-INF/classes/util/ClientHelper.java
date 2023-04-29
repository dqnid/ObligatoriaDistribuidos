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
	 * Static methods to keep the code clean
	 * returns 0 when successful
	 * returns < 0 when not
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
}