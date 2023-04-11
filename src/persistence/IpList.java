package persistence;

import java.util.ArrayList;

/*
 * Helper class to manage IPs
 * ips : {ip,port}
 * */
public class IpList {
	private ArrayList<String[]> ips;
	
	public IpList(){
		this.ips = new ArrayList<>();
	}
	
	public IpList(ArrayList<String[]> ips){
		this.ips = ips;
	}
	
	public void addIp(String ip, String port) {
		String[] str = {ip,port};
		this.ips.add(str);
	}
	
	public ArrayList<String[]> getArrayList(){
		return ips;
	}
	
	public String getString(){
		String formatedIps = "";
		for (String[] ip : ips){
			formatedIps += "" + ip[0] + ":" + ip[1] + ";";
		}
		return formatedIps;
	}
}
