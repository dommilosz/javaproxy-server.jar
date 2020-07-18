package com.dommilosz.main;

import com.dommilosz.utility.ioreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static com.dommilosz.utility.logger.log.*;

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
		try{
		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);
		writer.println(packettype + cmd);
		}catch (Exception ex) {clientStop();}
	}

	public static void readServer() {
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

				if (packettype.equals("RAW")) {
					log_Prefix = "[Remote]";
					log_time = "";
					WriteLine(cmd);
					log_Prefix = "#";
					log_time = "#";
					Thread.sleep(50);
				}
				if (packettype.equals("RAWERR")) {
					log_Prefix = "[Remote]";
					log_time = "";
					log_level = 21;
					WriteLine(cmd);
					log_Prefix = "#";
					log_time = "#";
					Thread.sleep(50);
				}
				if (packettype.equals("AUTH")) {
					if(cmd.equals("pass=true")){
						WriteLine("Server needs password to connect!");
						WriteLine("Provide password:");
						passmode = true;
					}
				}

				Thread.sleep(50);
			}
		}catch (Exception ex) {clientStop();}
	}
}
