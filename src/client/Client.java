package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import protocol.Code;

import org.json.*;

public class Client {

	public final static int PORT = 1676;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String userName;

	public Client() throws UnknownHostException, IOException {
		socket = new Socket("localhost", PORT);
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

	}

	public void login(String username) {
		userName = username;
		JSONObject request = new JSONObject();
		try {
			request.put(Code.TYPE_MESSAGE, Code.LOGIN);
			request.put(Code.USER_NAME, username);

			System.out.println("CLIENT SEND: " + request);
			writer.println(request);
			writer.flush(); // flush forza l段nvio di eventuali buffer in
							// memoria

			JSONObject response = new JSONObject(reader.readLine());
			System.out.println("LOGIN: " + response.getString(Code.STATUS).compareTo(Code.SUCCESS));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ECCEZIONE");
			e.printStackTrace();
		}

	}

	public void logout() {
		JSONObject request = new JSONObject();
		try {
			request.put(Code.TYPE_MESSAGE, Code.LOGOUT);
			request.put(Code.USER_NAME, this.userName);

			System.out.println("CLIENT SEND: " + request);
			writer.println(request);
			writer.flush(); // flush forza l段nvio di eventuali buffer in
							// memoria
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getUserList() throws JSONException, IOException {

		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.ONLINE_USERS);

		System.out.println("CLIENT SEND: " + request);

		writer.println(request);
		writer.flush(); // flush forza l段nvio di eventuali buffer in memoria
		JSONObject response = new JSONObject(reader.readLine());

		System.out.println("CLIENT RECEIVE: " + response);

		JSONArray array = response.getJSONArray(Code.USERS);
		for (int i = 0; i < array.length(); i++) {
			System.out.println(i + ": " + array.get(i));
		}

	}

	public void closeSocket() throws IOException {
		socket.close();
	}

	public void updateMessage() throws IOException, JSONException {

		JSONObject request = new JSONObject();
		request.put(Code.TYPE_MESSAGE, Code.MESSAGES_SYNC);
		writer.println(request);
		writer.flush();

		JSONArray array = new JSONArray(reader.readLine());
		for (int i = 0; i < array.length(); i++) {
			System.out.println(i + ": " + array.get(i));
		}

	}

	public void sendsMessage(String user, String message) {
		JSONObject request = new JSONObject();
		Set<String> users = new HashSet<String>();
		users.add(user);
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

}
