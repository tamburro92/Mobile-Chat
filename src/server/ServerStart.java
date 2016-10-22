package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class ServerStart {
	public final static int PORT = 1676;

	public static void main(String[] args) throws IOException {
		Map<String,ThreadedServerSocket> usersOnline=new HashMap<String,ThreadedServerSocket>();
		ServerSocket serverSocket = new ServerSocket(PORT);
		while (true) {
			Socket sock = serverSocket.accept();
			ThreadedServerSocket server=new ThreadedServerSocket(sock, usersOnline);
			server.start();
		}

	}

}
