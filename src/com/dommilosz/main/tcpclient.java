package com.dommilosz.main;

import com.dommilosz.utility.env;
import com.dommilosz.utility.ioreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static com.dommilosz.utility.iowriter.log.*;
import static com.dommilosz.main.tcphandler.*;

public class tcpclient {
	public static Socket socket;
	public static int socketport;
	public static boolean passmode = false;

	public static void startSync(String hostname, int port) {
		try {
			passmode = false;
			clientStop();
			socketport = port;
			socket = new Socket(hostname, port);
			Thread reader = new Thread(() -> {
				readServer();
				clientStop();
			});
			Thread keeper = new Thread(() -> {
				try {
					while (!socket.isClosed()) {
						writeServer("$", tcphandler.pktype.ka);
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					clientStop();
				}
			});
			keeper.start();
			reader.start();


		} catch (UnknownHostException ex) {

			System.out.println("Server not found: " + ex.getMessage());

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
		}
	}

	public static void clientStop() {
		try {
			socket.close();
			log_Prefix = "[Remote]";
			log_time = "";
			log_level = 21;
			WriteLine("Disconnected");
			log_Prefix = "#";
			log_time = "#";
		} catch (Exception e) {

		}
		socket = null;
	}

	public static void start(String hostname, int port) {
		Thread t = new Thread(() -> startSync(hostname, port));
		t.start();
	}

	public static void writeServer(String cmd, String packettype) {
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			String message = packet.createPacket(cmd, packettype);
			writer.println(message);
		} catch (Exception ex) {
			clientStop();
		}
	}

	public static void readServer() {
		try {
			InputStream inputraw = socket.getInputStream();
			Scanner sc = new Scanner(inputraw);
			while (!socket.isClosed()) {

				String pkcontent = ioreader.readLine(inputraw);
				packet p = new packet(pkcontent);

				if (p.checkType(pktype.raw)) {
					log_Prefix = "[Remote]";
					log_time = "";
					WriteLine(p.content);
					log_Prefix = "#";
					log_time = "#";
					Thread.sleep(50);
				}
				if (p.checkType(pktype.rawerr)) {
					log_Prefix = "[Remote]";
					log_time = "";
					log_level = 21;
					WriteLine(p.content);
					log_Prefix = "#";
					log_time = "#";
					Thread.sleep(50);
				}
				if (p.checkType(pktype.authinfo)) {
					if (p.content.equals("pass=true")) {
						WriteLine("Server needs password to connect!");
						WriteLine("Provide password:");
						passmode = true;
					}
					if (p.content.equals("auth=true")) {
						tcpclient.passmode = false;
					}
				}
				if (p.checkType(pktype.userinfo)) {
					if (p.content.equals("$")) {
						writeServer("0" + env.user(), pktype.userinfo);
						writeServer("1" + env.pcname(), pktype.userinfo);
					}
				}

				Thread.sleep(50);
			}
		} catch (Exception ex) {
			clientStop();
		}
	}
}
