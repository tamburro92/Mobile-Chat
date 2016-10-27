package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import protocol.Code;

import org.json.*;

public class Client {

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String userName;
	private boolean isLogged = false;

	public Client(String ip, int port) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

	}

	public boolean isLogged() {
		return isLogged;
	}

	public String getUserName() {
		return userName;
	}

	public synchronized void login(String username) throws JSONException, IOException {
		if (username.isEmpty())
			return;
		this.userName = username;
		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.LOGIN);
		request.put(Code.USER_NAME, username);

		System.out.println("CLIENT SEND: " + request);
		writer.println(request);
		writer.flush(); // flush forza l段nvio di eventuali buffer in
						// memoria

		JSONObject response = new JSONObject(reader.readLine());
		System.out.println("CLIENT RECEIVE: " + response);
		if (response.getString(Code.STATUS).compareTo(Code.SUCCESS) == 0)
			isLogged = true;

	}

	public synchronized void logout() throws JSONException, IOException {
		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.LOGOUT);
		request.put(Code.USER_NAME, this.userName);

		System.out.println("CLIENT SEND: " + request);
		writer.println(request);
		writer.flush(); // flush forza l段nvio di eventuali buffer in
						// memoria
		JSONObject response = new JSONObject(reader.readLine());
		System.out.println("CLIENT RECEIVE: " + response);
		isLogged = false;
		socket.close();

	}

	public synchronized List<String> getUserList() throws JSONException, IOException {
		List<String> listUser = new LinkedList();
		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.ONLINE_USERS);

		System.out.println("CLIENT SEND: " + request);

		writer.println(request);
		writer.flush(); // flush forza l段nvio di eventuali buffer in memoria
		JSONObject response = new JSONObject(reader.readLine());

		System.out.println("CLIENT RECEIVE: " + response);

		JSONArray array = response.getJSONArray(Code.USERS);
		for (int i = 0; i < array.length(); i++) {
			listUser.add(array.getString(i));
		}
		return listUser;

	}

	public synchronized void closeSocket() throws IOException {
		socket.close();
	}

	public synchronized JSONArray getMessagesRawFromServer() throws IOException, JSONException {

		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.MESSAGES_SYNC);
		writer.println(request);
		writer.flush();

		JSONArray array = new JSONArray(reader.readLine());
		System.out.println("CLIENT RECEIVE: " + array);
		return array;

	}

	public synchronized void sendsMessage(String user, String message) {
		Set<String> users=new HashSet<String>();
		users.add(user);
		sendsMessage(users,message);
	}

	public synchronized void sendsMessage(Set<String> users, String message) {
		JSONObject request = new JSONObject();

		try {
			request.put(Code.TYPE_MESSAGE, Code.SEND_MESSAGE);
			request.put(Code.SENDER, this.userName);
			request.put(Code.RECEIVERS, users);
			request.put(Code.MESSAGE, message);

			System.out.println("CLIENT SEND: " + request);
			writer.println(request);
			writer.flush(); // flush forza l段nvio di eventuali buffer in
							// memoria

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void sendsMessage(GroupUserList users, String message) {
		sendsMessage(users.getUsers(), message);
	}

	public synchronized Map<GroupUserList, String> getMessagesFromServer() throws IOException, JSONException {
		Map<GroupUserList, String>  mapMessages=new HashMap<>();
		JSONArray arrayMSG = getMessagesRawFromServer();
		for (int i = 0; i < arrayMSG.length(); i++) {
			GroupUserList group=new GroupUserList();
			String sender = arrayMSG.getJSONObject(i).getString(Code.SENDER);
			String message = sender + ": " + arrayMSG.getJSONObject(i).getString(Code.MESSAGE) + "\n";
			
			JSONArray receivers=(JSONArray) arrayMSG.getJSONObject(i).get(Code.RECEIVERS);
			
			group.addUser(sender);
			for(int j=0;j<receivers.length();j++){
				if(!receivers.getString(j).equals(userName)) //non aggiungiamo nel gruppo l'username
				   group.addUser(receivers.getString(j));
			}
			if(mapMessages.containsKey(group)){
				mapMessages.put(group, mapMessages.get(group)+message);
			}else{
				mapMessages.put(group,message);
			}
		}
		return mapMessages;
	}

}
