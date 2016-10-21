package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;

import protocol.Code;

import org.json.*;

public class Client {

	public final static int PORT=1677;
	

	public static void main(String[] args) throws UnknownHostException, IOException, JSONException {	
		
		Socket sock=new Socket("localhost",PORT);
		PrintWriter prw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-8"));
		
		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE,Code.LOGIN);
		request.put(Code.USER_NAME, "Antonio");

		System.out.println("CLIENT SEND: "+request);
		prw.println(request);
    	prw.flush(); //flush forza l’invio di eventuali buffer in memoria

    	BufferedReader brd=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-8"));
		System.out.println("CLIENT RICEVE: "+brd.readLine());
	    
	    
	}

	
}
