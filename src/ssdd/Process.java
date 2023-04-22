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

import util.ClientHelper;

import static util.Constants.TOMADA;

import java.util.ArrayList;

import static util.Constants.BUSCADA;
import static util.Constants.LIBERADA;

@Singleton
@Path("process")
public class Process {
	private int state; //Liberada=0, Buscada=1, Tomada=2
	private int c; //Contador
	private int p; //Id del proceso
	private String[] ipList; //Formato ip:puerto
	private String myIp; //Formato ip:puerto
	
	/*
	 * Service consumed to enter the SC
	 * */
	@Path("join")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String join(@DefaultValue("null") @QueryParam(value="t") String tj, @DefaultValue("0") @QueryParam(value="p") String pj){
		if (state == TOMADA || state == BUSCADA && this.isOlderEntrance(this.c, this.p, Integer.parseInt(tj), Integer.parseInt(pj))) {
			synchronized(this.getClass()) {
				try {
					this.getClass().wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return "go";		
	}
	
	/*
	 * Main class called to start
	 * TODO: Add error messages in case of failure
	 * */
	@Path("start")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String start(@DefaultValue("") @QueryParam(value="iplist") String iplist,@QueryParam(value="id") int id, @QueryParam(value="myIp") String myIp){
		this.ipList = iplist.split(","); //Es evitable si usamos un objeto para comunicar
		this.p = id;
		this.myIp = myIp;
		String response = "fallo";
		this.state = LIBERADA;
		this.c = 0;
		
		System.out.println("Soy " + id + " - " + this.myIp + " y conozco a " + iplist);
		
		if (ipList == null || this.p < 0) { return response; }
		
		/*
		 * "Multicast"
		 * TODO: use threads to launch
		 * */
		

		this.state = TOMADA;
		this.c+=1;
		
		/*
		 * Free SC
		 * */
		this.state = LIBERADA;
		synchronized(this.getClass()) {
			this.getClass().notifyAll();
		}
		
		return "finished";
	}
	
	private Boolean isOlderEntrance(int ti, int pi, int tj, int pj) {
		return ti < tj || ti == tj && pi < pj;
	}
}