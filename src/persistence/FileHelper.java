package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Helper class to manage all file IO
 * */
public class FileHelper {
	public static IpList getListIP(String route_to_file){
		IpList ipList = new IpList();
		File file = new File(route_to_file);
		if (file.exists()) {
			Scanner file_reader;
			try {
				file_reader = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			while (file_reader.hasNextLine()) {
                String line = file_reader.nextLine();
                if (line.startsWith("#")) {
                	continue;
                } else if (line.startsWith("id=")) {
                	line.substring(3);
                }
                String[] ip_port_values = line.split(":");
				ipList.addIp(ip_port_values[0],ip_port_values[1]);
			}
			file_reader.close();
			return ipList;
		}else {
			return null;
		}
	}
	
	/*
	 * Return an id read in config file
	 * */
	public static int getId(String route_to_file) {
		File file = new File(route_to_file);
		String id="-1";
		if (file.exists()) {
			Scanner file_reader;
			try {
				file_reader = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -1;
			}
			while (file_reader.hasNextLine()) {
	            String line = file_reader.nextLine();
	            if (line.startsWith("#")) {
	            	continue;
	            } else if (line.startsWith("id=")) {
	            	id = line.substring(3);
	            }
			}
			file_reader.close();
		}
		return Integer.parseInt(id);
	}
}
