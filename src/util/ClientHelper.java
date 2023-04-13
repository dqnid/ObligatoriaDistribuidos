package util;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/*
 * Helper class to manage HTTP requests
 * */
public class ClientHelper {
	public static String requestEntry(String ip, String port, int t, int p){
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ ip +":" + port + "/ssdd/").build();
		WebTarget target = client.target(uri);
		
		return target.path("process").path("join").queryParam("t", t).queryParam("p", p).request(MediaType.TEXT_PLAIN).get(String.class);
	}
	public static int startProcess(String ip, String port, String cfgfile) {
		Client client=ClientBuilder.newClient();
		URI uri=UriBuilder.fromUri("http://"+ ip +":" + port + "/ssdd/").build();
		WebTarget target = client.target(uri);
		if (target.path("process").path("start").queryParam("cfgfile", cfgfile).request(MediaType.TEXT_PLAIN).get(String.class).equals("finished")) {
			return 0;
		} else {
			return  -1;
		}
	}
}