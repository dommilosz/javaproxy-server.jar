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

import static com.dommilosz.utility.logger.log.*;
import static com.dommilosz.main.tcphandler.*;

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
			WriteLine("[SERVER] >> Remote server is listening on port " + port);
			if (password.equals("")) {
				log_level = 21;
				WriteLine("[SERVER] >> Server is not protected by password!");
				log_level = 21;
				WriteLine("[SERVER] >> Use: remote start %s <password>", port);
			}
			WriteLine("[SERVER] >> Server started!");
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
					writeClient(sc, "$", pktype.userinfo);
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
		String message = packet.createPacket(str, packettype);
		Socket socket = sc.socket;
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(message);
		} catch (Exception ex) {
			killSocket(sc);
		}
	}

	public static void writeAllClients(String str, String packettype) {
		String message = packet.createPacket(str, packettype);
		for (socketConnection sc : socketConnection.sockets) {
			if (sc.auth) {
				Socket socket = sc.socket;
				try {
					OutputStream output = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(output, true);
					writer.println(message);
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

				String pkcontent = ioreader.readLine(inputraw);
				packet p = new packet(pkcontent);
				if (p.checkType(pktype.userinfo)) {
					if (p.content.charAt(0) == '0') {
						sc.username = p.content.substring(1);
					}
					if (p.content.charAt(0) == '1') {
						sc.pcname = p.content.substring(1);
					}
				}
				if (!sc.username.equals("") && !sc.pcname.equals("")) {
					if (!sc.auth) {
						if (p.checkType(pktype.authpass)) {
							writeClient(sc, "pass=true", tcphandler.pktype.authinfo);
							if (p.content.equals(password)) {
								sc.auth = true;
								sc.invalidPassAttempts = 0;
								writeClient(sc, "Password is correct", tcphandler.pktype.raw);
								writeClient(sc, "auth=true", tcphandler.pktype.authinfo);
								WriteLine("[SERVER] >> New client connected");
							} else {
								sc.invalidPassAttempts += 1;
								writeClient(sc, "Password is incorrect ("+(4-sc.invalidPassAttempts)+" left)", tcphandler.pktype.rawerr);
								if (sc.invalidPassAttempts > 3) {
									kickSocket(sc, "Too many invalid password attempts");
								}else {
									writeClient(sc, "pass=true", tcphandler.pktype.authinfo);
								}
							}
						}
					}
					if (sc.auth) {
						if (p.checkType(pktype.cmd)) {
							commandhandler.Exec(p.content);
						}
						if (p.checkType(pktype.raw)) {
							WriteLine(p.type);
						}
					}


					Thread.sleep(50);

				}
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

	public static void kickSocket(socketConnection sc, String reason) {
		writeClient(sc, "Kicked by server:", pktype.rawerr);
		writeClient(sc, reason, pktype.rawerr);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		killSocket(sc);
	}

}
