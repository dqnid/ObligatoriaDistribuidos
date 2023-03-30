package obligatoria;

import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("sc")
public class ClientServer {
	private static int estado; //Liberada=0, Buscada=1, Tomada=2 
	@Path("peticion")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String peticion(@DefaultValue("0") @QueryParam(value="t") long t, @DefaultValue("0") @QueryParam(value="p") int p){
		return "info";
	}
	
	public static void setEstado(int estado) {
		ClientServer.estado = estado;
	}
}