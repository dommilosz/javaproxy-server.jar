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

public class tcpserver {

	public static ServerSocket serverSocket;
	public static List<Socket> socketList = new ArrayList<Socket>();
	public static int socketport;

	public static void startSync(int port) {
		try {
			serverStop();
			serverSocket = new ServerSocket(port);
			System.out.println("Server is listening on port " + port);
			socketport = port;
			while (true) {
				Socket socket = serverSocket.accept();
				try {
					Thread reader = new Thread(() -> {
						readClient(socket);
					});
					Thread keeper = new Thread(() -> {
						sendKeepAlive(socket);
					});

					keeper.start();
					reader.start();
					socketList.add(socket);
					WriteLine("[SERVER] >> New client connected");
				} catch (Exception ex) {
					killSocket(socket);
				}
			}

		} catch (Exception ex) {

		}
	}

	public static void sendKeepAlive(Socket socket) {
		while (!socket.isClosed()) {
			writeClient(socket, "$", tcphandler.pktype.ka);
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
		for (Socket s : socketList) {
			try {
				s.close();
			} catch (Exception ex) {

			}
		}
	}

	public static void start(int port) {
		Thread t = new Thread(() -> startSync(port));
		t.start();
	}

	public static void writeClient(Socket socket, String str, String packettype) {
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(packettype + str);
		} catch (Exception ex) {
			killSocket(socket);
		}
	}

	public static void writeAllClients(String str, String packettype) {
		for (Socket socket : socketList) {
			try {
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);
				writer.println(packettype + str);
			} catch (Exception ex) {
				killSocket(socket);
			}
		}
	}

	public static void readClient(Socket socket) {
		try {
			InputStream inputraw = socket.getInputStream();
			Scanner sc = new Scanner(inputraw);
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

				if (packettype.equals("CMD")) {
					commandhandler.Exec(cmd);
				}
				if (packettype.equals("RAW")) {
					WriteLine(cmd);
				}

				Thread.sleep(50);

			}
		} catch (Exception ex) {
			killSocket(socket);
		}
	}

	public static void killSocket(Socket socket) {
		try {
			socket.close();
			socketList.remove(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
