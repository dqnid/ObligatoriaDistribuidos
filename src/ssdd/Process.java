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

@Singleton
@Path("process")
public class Process {
	private static int state; //Liberada=0, Buscada=1, Tomada=2 
	
	@Path("hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String petition(@DefaultValue("null") @QueryParam(value="msg") String msg){
		return msg;
	}
	
	/*
	 * Main class called to start
	 * */
	@Path("main")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String main(@DefaultValue("./config.cfg") @QueryParam(value="cfgfile") String cfgfile){
		IpList ipList = FileHelper.getListIP(cfgfile);
		String response = "fallo";
		if (ipList == null) {
			return response;
		}
		for (String[] direction : ipList.getArrayList()) {
			response = ClientHelper.requestEntry(direction[0], direction[1]);			
		}
		return response;
	}
}