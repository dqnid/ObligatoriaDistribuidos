package obligatoria;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("node")
public class ClientServer {
	@Path("pedirInfo")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String pedirInfo(){
		return "info";
	}
}
