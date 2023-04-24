package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Helper class to manage all file IO
 * */
public class FileHelper {
	public static int log(String route,int id, char action, long time) {
   	 	String newline = System.getProperty("line.separator");
	    try {
			File file = new File(route);
	        file.createNewFile();
	        try {
	        	FileWriter myWriter = new FileWriter(route,true);
	        	myWriter.append("P" + id + " " + action + " " + time);
	        	myWriter.append(newline);
	        	myWriter.close();
	        } catch (IOException e) {
	        	System.out.println("Error en la escritura del log.");
	        	e.printStackTrace();
	        }
	      } catch (IOException e) {
	        System.out.println("Error en la apertura del log.");
	        e.printStackTrace();
	        return -1;
	      }
		return 0;
	}
	
	public static ArrayList<String> getListIP(String route_to_file){
		ArrayList<String> ipList = new ArrayList<>();
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
                }
                ipList.add(line);
     
			}
			file_reader.close();
			return ipList;
		}else {
			return null;
		}
	}
	
	public static String getFormatedIPList(String route_to_file, String ip){
		String ipList = "";
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
                if (line.startsWith("#") || line.startsWith(ip))
                	continue;
                if (ipList == "")
                	ipList += line;
                else 
                	ipList += "," + line;
			}
			file_reader.close();
			return ipList;
		}else {
			return null;
		}
	}
}
