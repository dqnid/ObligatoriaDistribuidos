package obligatoria;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextMain implements ServletContextListener{
	@Override
	public
	void contextInitialized(ServletContextEvent sce) {
		System.out.println("===========================\n\n\n Servidor inicializado \n\n\n===========================");
	}
}
