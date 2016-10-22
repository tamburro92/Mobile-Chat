package client;

import java.io.IOException;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ClientTest {	

	public static void main(String[] args) throws UnknownHostException, IOException, JSONException, InterruptedException {	
	
		final int PORT = 1676;
		final String ip = "localhost";


		Client c1=new Client(ip,PORT);
		c1.login("Arturo");
		c1.getUserList();
		
		Client c2=new Client(ip,PORT);
		c2.login("Babbala");
		c2.getUserList();
		c2.sendsMessage("Arturo", "CIAO AMICO");
		c2.sendsMessage("Arturo", "HOOLAAAA");

		Thread.sleep(3000);
		c1.updateMessage();
		c1.logout();
		
		c2.logout();

	
	    
	    
	}
	
}
