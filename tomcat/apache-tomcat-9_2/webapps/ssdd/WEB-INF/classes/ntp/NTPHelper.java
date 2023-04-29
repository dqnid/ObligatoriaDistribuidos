package ntp;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class NTPHelper {
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
			
			offset = determinarOffset(t0,t1,t2,t3);
			delay = determinarDelay(t0,t1,t2,t3);
			if (i == 0 || delay < bestPair[1]) {
				bestPair[0] = offset;
				bestPair[1] = delay;
			}
		}
		return bestPair;
	}
	private static long determinarOffset(long t0, long t1, long t2, long t3) {
		return ((t1-t0+t2-t3)/2);
	}
	
	private static long determinarDelay(long t0, long t1, long t2, long t3) {
		return (t1-t0+t3-t2);
	}
}
