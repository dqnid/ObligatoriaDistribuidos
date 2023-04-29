package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
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
	        	return -1;
	        }
	      } catch (IOException e) {
	        System.out.println("Error en la apertura del log.");
	        e.printStackTrace();
	        return -1;
	      }
		return 0;
	}
	
	public static int adjustLog(String route_to_file, long delay){
		File file = new File(route_to_file);
	
		String output_route_to_file = "" + route_to_file.split(".l")[0] + "_adjusted.log";
		
		File file_output = new File(output_route_to_file);
   		
   	 	String newline = System.getProperty("line.separator");

		if (file.exists()) {
			Scanner file_reader;
			try {
				file_reader = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -1;
			}
			while (file_reader.hasNextLine()) {
                String[] line = file_reader.nextLine().split(" ");
                long time = Long.parseLong(line[2]);
                time += delay;
                try {
                    file_output.createNewFile();
    	        	FileWriter myWriter = new FileWriter(output_route_to_file,true);
    	        	myWriter.append("" + line[0] + " " + line[1] + " " + time);
    	        	myWriter.append(newline);
    	        	myWriter.close();
    	        } catch (IOException e) {
    	        	System.out.println("Error en la escritura del log.");
    				file_reader.close();
    	        	e.printStackTrace();
    	        	return -1;
    	        }
			}
			file_reader.close();
			return 0;
		}else {
			return -1;
		}
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
                if (line.startsWith("ntp") || line.startsWith("#")) {
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
	
	public static String getFormatedIPList(String route_to_file, String destination_ip){
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
                if (line.startsWith("ntp") || line.startsWith("#") || line.startsWith(destination_ip))
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
	
	public static String getNTPServer(String route_to_file){
		String ntpServer="";
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
                if (line.startsWith("ntp="))
                {
                	ntpServer = line.split("=")[1];
                	break;
                }
                	
			}
			file_reader.close();
			return ntpServer == "" ? null : ntpServer;
		}else {
			return null;
		}
	}
	
	//no funciona
	public static boolean folderExists(String route_to_folder) {
		Path path = Paths.get(route_to_folder);
		return Files.exists(path);
	}
}
