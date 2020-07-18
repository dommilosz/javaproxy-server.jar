package com.dommilosz.main;

import com.dommilosz.utility.ioreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class tcpserver {

	public static ServerSocket serverSocket;
	//public static List<Socket> socketList = new ArrayList<Socket>();
	public static int socketport;
	public static String password = "root";

	public static void startSync(int port, String pass) {
		try {
			password = pass;
			serverStop();
			serverSocket = new ServerSocket(port);
			WriteLine("Server is listening on port " + port);
			if (password.equals("")) {
				log_level = 21;
				WriteLine("Server is not protected by password!");
				log_level = 21;
				WriteLine("Use: remote start %s <password>", port);
			}
			WriteLine("Server started!");
			socketport = port;
			while (true) {
				Socket socket = serverSocket.accept();
				socketConnection sc = new socketConnection(socket);
				try {

					Thread reader = new Thread(() -> {
						readClient(sc);
					});
					Thread keeper = new Thread(() -> {
						sendKeepAlive(sc);
					});

					keeper.start();
					reader.start();
					WriteLine("[SERVER] >> New client connecting");
					if (!password.equals("")) {
						writeClient(sc, "pass=true", tcphandler.pktype.authinfo);
					} else {
						sc.auth = true;
						WriteLine("[SERVER] >> New client connected");
					}
				} catch (Exception ex) {
					killSocket(sc);
				}
			}

		} catch (Exception ex) {

		}
	}

	public static void sendKeepAlive(socketConnection sc) {
		Socket socket = sc.socket;
		while (!socket.isClosed()) {
			writeClient(sc, "$", tcphandler.pktype.ka);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void serverStop() {
		try {
			serverSocket.close();
		} catch (Exception ex) {

		}
		for (socketConnection sc : socketConnection.sockets) {
			Socket socket = sc.socket;
			try {
				socket.close();
				socketConnection.sockets.remove(sc);
			} catch (Exception ex) {

			}
		}

	}

	public static void start(int port, String pass) {
		Thread t = new Thread(() -> startSync(port, pass));
		t.start();
	}

	public static void writeClient(socketConnection sc, String str, String packettype) {
		Socket socket = sc.socket;
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(packettype + str);
		} catch (Exception ex) {
			killSocket(sc);
		}
	}

	public static void writeAllClients(String str, String packettype) {
		for (socketConnection sc : socketConnection.sockets) {
			if (sc.auth) {
				Socket socket = sc.socket;
				try {
					OutputStream output = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(output, true);
					writer.println(packettype + str);
				} catch (Exception ex) {
					killSocket(sc);
				}
			}
		}
	}

	public static void readClient(socketConnection sc) {
		Socket socket = sc.socket;
		try {

			InputStream inputraw = socket.getInputStream();
			while (!socket.isClosed()) {

				String cmd = ioreader.readLine(inputraw);
				String packettype = "RAW";
				if (cmd.contains(tcphandler.pktype.any)) {
					String[] args = cmd.split("#");
					if (args[0].equals(tcphandler.pktype.any)) {
						packettype = args[1];
						cmd = cmd.replace(args[0] + "#" + args[1] + "#", "");
					}
				}
				cmd = cmd.trim();
				if (!sc.auth) {
					if (packettype.equals("AUTHPASS")) {
						if (cmd.equals(password)) {
							sc.auth = true;
							writeClient(sc, "Password is correct", tcphandler.pktype.raw);
							WriteLine("[SERVER] >> New client connected");
						} else {
							writeClient(sc, "Password is incorrect", tcphandler.pktype.rawerr);
							writeClient(sc, "pass=true", tcphandler.pktype.authinfo);
						}
					}
				}
				if (sc.auth) {
					if (packettype.equals("CMD")) {
						commandhandler.Exec(cmd);
					}
					if (packettype.equals("RAW")) {
						WriteLine(cmd);
					}
				}


				Thread.sleep(50);

			}
		} catch (Exception ex) {
			killSocket(sc);
		}
	}

	public static void killSocket(socketConnection sc) {
		Socket socket = sc.socket;
		try {
			socket.close();
			socketConnection.sockets.remove(sc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class socketConnection {
		public static List<socketConnection> sockets = new ArrayList<socketConnection>();
		public Socket socket;
		public boolean auth = false;

		public socketConnection(Socket s) {
			socket = s;
			sockets.add(this);
		}
	}
}
