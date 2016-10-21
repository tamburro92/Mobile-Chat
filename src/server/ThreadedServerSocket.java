package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import org.json.*;

import protocol.Code;

public class ThreadedServerSocket extends Thread {

	private Socket socket;
	private Map<String, ThreadedServerSocket> usersOnline;
	private BufferedReader reader;
	private PrintWriter writer;

	public ThreadedServerSocket(Socket s, Map<String, ThreadedServerSocket> usersOnline) {
		this.socket = s;
		this.usersOnline = usersOnline;

	}

	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			while (true) {
				JSONObject message = new JSONObject(reader.readLine());
				System.out.println("SERVER RECEIVE: "+message);

				String typeOperation = message.getString(Code.TYPE_MESSAGE);

				switch (typeOperation) {
				case Code.LOGIN:
					login(message);
					break;
				case Code.LOGOUT:
					// fare login
					break;
				case Code.ONLINE_USERS:

					break;

				}
			}
		} catch (IOException e) {
			System.out.println("ECCEZIONE IOException");

			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("ECCEZIONE JSON");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

	}

	private JSONObject readJson(BufferedReader read) throws IOException, JSONException {
		JSONObject json = new JSONObject(read.readLine());
		return json;

	}

	private void login(JSONObject message) throws JSONException {
		String name = message.getString(Code.USER_NAME);
		JSONObject response = new JSONObject();
		response.put(Code.TYPE_MESSAGE, Code.LOGIN);
		if (usersOnline.containsKey(name)) {
			response.put(Code.STATUS, Code.FAILED);
		} else {
			response.put(Code.STATUS, Code.SUCCESS);

		}
		writer.println(response);
		writer.flush();
	}

}
