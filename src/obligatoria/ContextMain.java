package obligatoria;

import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import util.Constantes;
import util.Redes;

public class ContextMain implements ServletContextListener{
	
	ArrayList<String> dirs = new ArrayList<>();
	
	@Override
	public
	void contextInitialized(ServletContextEvent sce) {
		System.out.println("===========================\n\n\n Servidor inicializado\n En la IP: " + Redes.getCurrentIp() +" \n\n\n===========================");
		for (String ip : Constantes.Ips){
			
		}
		ClientServer.setEstado(Constantes.Liberada);
	}
}