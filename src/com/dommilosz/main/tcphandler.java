package com.dommilosz.main;

import com.dommilosz.utility.array;
import com.dommilosz.utility.ioreader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static com.dommilosz.utility.logger.log.*;

public class tcphandler {
	public static int socketport = 0;
	public static Thread[] threads = new Thread[0];
	static Socket socket;
	static ServerSocket serverSocket;

	public static boolean socketConnected() {
		if (socket == null) {
			return false;
		}
		return socket.isConnected();
	}

	public static boolean socketOpen() {
		return socket != null && !socket.isClosed();
	}

	public static boolean serverOpen() {
		return serverSocket != null && !serverSocket.isClosed();
	}

	public static boolean anyOpen() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			return true;
		}
		return socket != null && !socket.isClosed();
	}

	public static void tcpStop() {
		try {
			socket.close();
		} catch (Exception ex) {
		}
		try {
			serverSocket.close();
		} catch (Exception ex) {
		}
		socket = null;
		serverSocket = null;
		killThreads();
	}

	public static void socketStop() {
		try {
			socket.close();
		} catch (Exception ex) {
		}
		socket = null;
		killThreads();
	}

	public static void killThreads() {
		try {
			for (Thread t : threads) {
				try {
					t.stop();
				} catch (Exception ex) {
				}
			}
		} catch (Exception ex) {
		}
		threads = new Thread[0];

	}

	public static String sockettype() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			return "server";
		}
		if (socket != null && !socket.isClosed()) {
			return "client";
		}
		return "closed";
	}

	public static boolean typeServer() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			return true;
		}
		if (socket != null && !socket.isClosed()) {
			return false;
		}
		return false;
	}

	public static boolean canIO() {
		return socketOpen() && socketConnected();
	}

	public static class pktype {
		public static String any = "95PTZSCJHD";
		public static String cmd = "95PTZSCJHD#CMD#";
		public static String raw = "95PTZSCJHD#RAW#";
		public static String ka = "95PTZSCJHD#KEEPALIVE#";
	}

	public static class tcpserver {

		public static void startSync(int port) {
			try {
				tcpStop();
				serverSocket = new ServerSocket(port);
				System.out.println("Server is listening on port " + port);
				socketport = port;
				while (true) {

					try {
						socket = serverSocket.accept();
						Thread reader = new Thread(() -> {
							try {
								readClient();
							} catch (Exception ex) {
								socketStop();
							}
						});
						Thread keeper = new Thread(() -> {
							try {
								while (true) {
									writeClient("$", pktype.ka);
									Thread.sleep(1000);
								}
							} catch (Exception ex) {

							}
						});
						keeper.start();
						reader.start();
						threads = array.add(threads, keeper);
						threads = array.add(threads, reader);
						WriteLine("[SERVER] >> New client connected");
					} catch (Exception ex) {
					}
				}

			} catch (Exception ex) {
				System.out.println("Server exception: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		public static void start(int port) {
			Thread t = new Thread(() -> startSync(port));
			t.start();
		}

		public static void writeClient(String str, String packettype) throws IOException, InterruptedException {
			if (!canIO() || !typeServer()) return;
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(packettype + str);
		}

		public static void readClient() throws Exception {
			if (!canIO() || !typeServer()) return;
			InputStream inputraw = socket.getInputStream();
			Scanner sc = new Scanner(inputraw);
			while (socket.isConnected()) {

				String cmd = ioreader.readLine(inputraw);
				String packettype = "RAW";
				if (cmd.contains(pktype.any)) {
					String[] args = cmd.split("#");
					if (args[0].equals(pktype.any)) {
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
		}
	}

	public static class tcpclient {

		public static void startSync(String hostname, int port) {
			try {
				tcpStop();
				socketport = port;
				socket = new Socket(hostname, port);
				Thread reader = new Thread(() -> {
					try {
						readServer();
					} catch (Exception ex) {
						tcpStop();
					}
				});
				Thread keeper = new Thread(() -> {
					try {
						while (true) {
							writeServer("$", pktype.ka);
							Thread.sleep(1000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				keeper.start();
				reader.start();
				threads = array.add(threads, keeper);
				threads = array.add(threads, reader);
				System.out.println("New client connected");


			} catch (UnknownHostException ex) {

				System.out.println("Server not found: " + ex.getMessage());

			} catch (IOException ex) {

				System.out.println("I/O error: " + ex.getMessage());
			}
		}

		public static void start(String hostname, int port) {
			Thread t = new Thread(() -> startSync(hostname, port));
			t.start();
		}

		public static void writeServer(String cmd, String packettype) throws IOException, InterruptedException {
			if (!canIO() || typeServer()) return;
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(packettype + cmd);
		}

		public static void readServer() throws Exception {
			if (!canIO() || typeServer()) return;
			InputStream inputraw = socket.getInputStream();
			Scanner sc = new Scanner(inputraw);
			while (socket.isConnected()) {

				String cmd = ioreader.readLine(inputraw);
				String packettype = "RAW";
				if (cmd.contains(pktype.any)) {
					String[] args = cmd.split("#");
					if (args[0].equals(pktype.any)) {
						packettype = args[1];
						cmd = cmd.replace(args[0] + "#" + args[1] + "#", "");
					}
				}

				if (packettype.equals("RAW")) {
					log_Prefix = "[Remote]";
					log_time = "";
					WriteLine(cmd);
					log_Prefix = "#";
					log_time = "#";
					Thread.sleep(50);
				}

				Thread.sleep(50);
			}
		}
	}
}

