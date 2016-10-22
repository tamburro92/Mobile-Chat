package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.*;

import protocol.Code;

public class ThreadedServerSocket extends Thread {

	private Socket socket;
	private Map<String, ThreadedServerSocket> usersOnline;
	private BufferedReader reader;
	private PrintWriter writer;
	private String userName;
	private List<JSONObject> messageToSyn;
	private boolean clientLogout=false;

	public ThreadedServerSocket(Socket s, Map<String, ThreadedServerSocket> usersOnline) {
		this.socket = s;
		this.usersOnline = usersOnline;
		this.messageToSyn = new LinkedList<JSONObject>();

	}

	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			while (!clientLogout) {
				String messageString = reader.readLine();
				
				JSONObject message = new JSONObject(messageString);
				System.out.println("SERVER RECEIVE: " + message);

				String typeOperation = message.getString(Code.TYPE_MESSAGE);

				switch (typeOperation) {
				case Code.LOGIN:
					login(message);
					break;
				case Code.LOGOUT:
					logout();
					break;
				case Code.ONLINE_USERS:
					listUsersOnline();
					break;
				case Code.SEND_MESSAGE:
					sendMessageToOtherUsers(message);
					break;
				case Code.MESSAGES_SYNC:
					syncOldMessage();
					break;
				}
			}

		} catch (IOException e) {
			System.out.println("ECCEZIONE IOException");

			// e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("ECCEZIONE JSON");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} 
		catch(NullPointerException e){//si verifica quando il client si disconnette senza fare il logout   
			//ed la reader.readLine() del server restituisce NULL
			System.out.println("ECCEZIONE NullPointerException");

		}
		finally {
			try {//LOGOUT e CLOSE SOCKET
				if (usersOnline.containsKey(this.userName))
					usersOnline.remove(this.userName);
				socket.close();
			} catch (IOException e) {
			}
		}

	}

	private void syncOldMessage() {
		JSONArray response = new JSONArray();
		response.put(this.messageToSyn);
		this.messageToSyn.clear();

		System.out.println("SERVER SEND: " + response);

		writer.println(response);
		writer.flush();

	}

	private void sendMessageToOtherUsers(JSONObject message) throws JSONException {
		JSONArray receivers = message.getJSONArray(Code.RECEIVERS);
		for (int i = 0; i < receivers.length(); i++) {
			String user = receivers.getString(i);
			if (this.usersOnline.containsKey(user)) {
				ThreadedServerSocket socketUser = this.usersOnline.get(user);
				socketUser.saveMessageToSync(message.getString(Code.SENDER), message.getString(Code.MESSAGE));
			}
		}
	}

	public void saveMessageToSync(String sender, String message) throws JSONException {
		JSONObject response = new JSONObject();
		response.put(Code.TYPE_MESSAGE, Code.SEND_MESSAGE);
		response.put(Code.SENDER, sender);
		response.put(Code.MESSAGE, message);
		System.out.println("SERVER SEND: " + response);
		messageToSyn.add(response);

	}

	private void listUsersOnline() throws JSONException {
		JSONObject response = new JSONObject();
		response.put(Code.TYPE_MESSAGE, Code.ONLINE_USERS);
		response.put(Code.USERS, this.usersOnline.keySet());

		System.out.println("SERVER SEND: " + response);
		writer.println(response);
		writer.flush();

	}

	private void login(JSONObject message) throws JSONException {
		String name = message.getString(Code.USER_NAME);
		JSONObject response = new JSONObject();
		response.put(Code.TYPE_MESSAGE, Code.LOGIN);
		if (usersOnline.containsKey(name)) {
			response.put(Code.STATUS, Code.FAILED);
		} else {
			this.usersOnline.put(name, this);
			response.put(Code.STATUS, Code.SUCCESS);
			this.userName = name;
		}
		System.out.println("SERVER SEND: " + response);

		writer.println(response);
		writer.flush();
	}

	private void logout() throws JSONException, IOException {
		// String name = message.getString(Code.USER_NAME);
		JSONObject response = new JSONObject();
		response.put(Code.TYPE_MESSAGE, Code.LOGOUT);
		if (usersOnline.containsKey(this.userName))
			usersOnline.remove(this.userName);

		response.put(Code.STATUS, Code.SUCCESS);
		System.out.println("SERVER SEND: " + response);

		writer.println(response);
		writer.flush();
		clientLogout=true;
	}

}
