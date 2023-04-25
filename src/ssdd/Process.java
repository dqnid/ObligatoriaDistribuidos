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
	
	@Path("prueba")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String prueba(){
		return "El server arranca";
	}
	
	/*
	 * Service consumed to enter the SC
	 * */
	@Path("join")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String join(@DefaultValue("0") @QueryParam(value="t") int tj, @DefaultValue("0") @QueryParam(value="p") int pj){
		//this.c = (tj < this.c ? this.c : tj) +1; //Ajuste tel tiempo lógico de Lamport diapo 33 tema 4
		if (state == TOMADA || state == BUSCADA && this.selfHasOlderEntrance(this.c, this.p, tj, pj)) {
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
	 * */
	@Path("start")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String start(@DefaultValue("") @QueryParam(value="ipList") String ipList,@DefaultValue("0") @QueryParam(value="id") int id,@QueryParam(value="myIp") String myIp){
		this.ipList = ipList.split(","); //Es evitable si usamos un objeto para comunicar
		this.p = id;
		this.myIp = myIp;
		this.state = LIBERADA;
		this.c = 0;
				
		if (ipList == null || this.p < 0) { return "Fallo"; }
		
		for (int i = 0; i < 100; i++) 
		{	
			this.state = BUSCADA;
			try {
				Thread.sleep((long)Math.random()*200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			int response = ClientHelper.requestEntry(this.ipList, this.c, this.p);
			if (response < 0) {
				return "Fallo";
			}
	
			this.state = TOMADA;
			this.c+=1;
			FileHelper.log("/home/danih/.config/ssddlog.log", this.p, 'E', System.currentTimeMillis());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			FileHelper.log("/home/danih/.config/ssddlog.log", this.p, 'S', System.currentTimeMillis());
			System.out.println("Soy " + this.p + " y entro en la zona");

			/*
			 * Free SC
			 * */
			this.state = LIBERADA;
			synchronized(this.getClass()) {
				this.getClass().notifyAll();
			}
		}
		
		return "finished";
	}
	
	private Boolean selfHasOlderEntrance(int ti, int pi, int tj, int pj) {
		return ti < tj || ti == tj && pi < pj;
	}
}