package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Helper class to manage all file IO
 * */
public class FileHelper {
	public static String getListIP(String route_to_file){
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
                if (line.startsWith("#")) {
                	continue;
                }
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
