/*
 * Daniel Heras Quesada
 * Guillermo Vicente González
 * Sistemas Distribuidos
 * Universidad de Salamanca
 * */
package ssdd;

import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import persistence.FileHelper;
import util.ClientHelper;

import static util.Constants.TOMADA;
import static util.Constants.BUSCADA;
import static util.Constants.LIBERADA;

import java.util.Random;

@Singleton
@Path("node")
public class Node {
	private int state; //Liberada=0, Buscada=1, Tomada=2
	private int ti;
	private int ci;
	private int p;
	private String[] ipList;
	private long[] ntp_values = {0,0};
	
	/*
	 * Service consumed to enter the SC
	 * */
	@Path("join")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String join(@DefaultValue("0") @QueryParam(value="t") int tj, @DefaultValue("0") @QueryParam(value="p") int pj){
		this.ci = (tj < this.ci ? this.ci : tj) + 1;
		if (state == TOMADA || state == BUSCADA && (ti < tj || ti == tj && this.p < pj)) {
			synchronized(this.getClass()) {
				try {
					this.getClass().wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return "System error on wait() call";
				}
			}
		}
		return "go";
	}
	
	/*
	 * Main class called to start
	 * */
	@Path("start")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String start(@QueryParam(value="ipList") String ipList,@QueryParam(value="id") int id,@QueryParam(value="logFolder") String logFolder,@QueryParam(value="ntpServer") String ntpServer){
		long[] ntp_values_pre;
		long[] ntp_values_post;
		
		this.ipList = ipList.split(",");
		this.p = id;
		this.state = LIBERADA;
		this.ci = 0;
				
		if (ipList == null || this.p < 0) { return "failed"; }
		
		ntp_values_pre = ClientHelper.requestNTP(ntpServer, 10);
		Random rand = new Random();
		
		for (int i = 0; i < 100; i++) 
		{	
			this.state = BUSCADA;
			this.ti = this.ci;
			try {
				int rand_time = rand.nextInt((500 - 300) + 1) + 300;
				Thread.sleep(rand_time);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			int response = ClientHelper.requestEntry(this.ipList, this.ti, this.p);
			if (response < 0) {
				return "failed";
			}
	
			this.state = TOMADA;
			this.ci+=1;
			FileHelper.log(""+logFolder+"/"+this.p+".log", this.p, 'E', System.currentTimeMillis());
			try {
				int rand_time = rand.nextInt((300 - 100) + 1) + 100;
				Thread.sleep((long)rand_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			FileHelper.log(""+logFolder+"/"+this.p+".log", this.p, 'S', System.currentTimeMillis());

			/*
			 * Free SC
			 * */
			this.state = LIBERADA;
			synchronized(this.getClass()) {
				this.getClass().notifyAll();
			}
		}

		ntp_values_post = ClientHelper.requestNTP(ntpServer, 10);
		this.ntp_values[0] = (ntp_values_pre[0] + ntp_values_post[0]) / 2;
		this.ntp_values[1] = (ntp_values_pre[1] + ntp_values_post[1]) / 2;

		FileHelper.adjustLog(""+logFolder+"/"+this.p+".log", this.ntp_values[1]);
		
		String log = FileHelper.getLog(""+logFolder+"/"+this.p+".log" + "_adjusted.log");
		
		return log;
	}
	
	@Path("getntp")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getntp(){
		long[]  t = {0,0};

		t[0] = System.currentTimeMillis();
		t[1] = System.currentTimeMillis();
		return ""+t[0]+";"+t[1];
	}
}