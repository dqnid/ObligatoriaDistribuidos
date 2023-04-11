package ssdd;

import java.io.IOException;

import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import persistence.FileHelper;
import persistence.IpList;
import util.ClientHelper;

import static util.Constants.TOMADA;
import static util.Constants.BUSCADA;
import static util.Constants.LIBERADA;

@Singleton
@Path("process")
public class Process {
	private int state; //Liberada=0, Buscada=1, Tomada=2
	private int c;
	private int p;
	private IpList ipList;
	
	@Path("hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String petition(@DefaultValue("null") @QueryParam(value="msg") String msg){
		return msg;
	}
	
	@Path("join")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String join(@DefaultValue("null") @QueryParam(value="t") int t, @DefaultValue("0") @QueryParam(value="p") int p){
		if (state != TOMADA) {
			return "go";
		}
		return "go";
	}
	
	/*
	 * Main class called to start
	 * */
	@Path("main")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String main(@DefaultValue("./config.cfg") @QueryParam(value="cfgfile") String cfgfile){
		this.ipList = FileHelper.getListIP(cfgfile);
		String response = "fallo";
		this.state = LIBERADA;
		this.c = 0;
		this.p = 1; //Configure in config.cfg
		
		if (ipList == null) { return response; }
		
		for (String[] direction : ipList.getArrayList()) {
			response = ClientHelper.requestEntry(direction[0], direction[1],this.c, this.p);			
		}
		return response;
	}
}