package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Redes {
	public static String getCurrentIp() {
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        return "" + localhost.getHostAddress().trim();
	}
}
