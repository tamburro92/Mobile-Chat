package client;

import java.io.IOException;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ClientTest {	

	public static void main(String[] args) throws UnknownHostException, IOException, JSONException, InterruptedException {	
	
		final int PORT = 1676;
		final String ip = "localhost";
		
		Client c1=new Client(ip,PORT);
		Client c2=new Client(ip,PORT);
		Client c3=new Client(ip,PORT);
		c1.login("Arturo");
		c2.login("Babbala");
		c3.login("Antonio");
		
		GroupUserList group1=new GroupUserList();
		group1.addUser("Babbala");
		c1.sendsMessage(group1, "Ciao");
		
		Map<GroupUserList, String> messaggi=c2.getMessagesFromServer();
		for(GroupUserList i:messaggi.keySet())
			System.out.println(i+"__"+messaggi.get(i));
		
		messaggi=c3.getMessagesFromServer();
		for(GroupUserList i:messaggi.keySet())
			System.out.println(messaggi.get(i));


	
		c1.logout();
		c2.logout();
		c3.logout();

		GroupUserList group2=new GroupUserList();
		group2.addUser("Arturo");group2.addUser("Babbala");group2.addUser("Antonio");


		
		GroupUserList group3=new GroupUserList();
		group1.addUser("Artsduro");group1.addUser("Babbala");group1.addUser("Antonio");
		System.out.println(group3.equals(group1));

		


/*
		Client c1=new Client(ip,PORT);
		c1.login("Arturo");
		c1.getUserList();
		
		Client c2=new Client(ip,PORT);
		c2.login("Babbala");
		c2.getUserList();
		c2.sendsMessage("Arturo", "CIAO AMICO");
		c2.sendsMessage("Arturo", "HOOLAAAA");

		Thread.sleep(3000);
		c1.getMessagesFromServer();
		c1.logout();
		
		c2.logout();

	*/
	    
	    
	}
	
}
