package ssdd;

import persistence.FileHelper;

public class Launcher {
	public static void main(String[] args) {
		//Leemos la lista de ip del fichero de config, que luego tendremos que paremetrizar o algo
		String ipList = FileHelper.getListIP("/home/danih/.config/ssddcfg.cfg");
		
		/* Separamos las ips en un array 
		 * [ ip:puerto;id ]
		 */
		String[] temp = ipList.split(",");
		
		/*
		 * Recorremos la lista de IPs
		 * */
		for (String str : temp) {
			/*
			 * Antes de contactar con el servidor (GET) separamos el identificador
			 * Al servidor le enviamos
			 * - lista de ips formateada
			 * - ip:puerto
			 * - id
			 * */
			String[] tmp = str.split(";");
			System.out.println("" + tmp[0] + " - " + tmp[1]);
		}
	}
}