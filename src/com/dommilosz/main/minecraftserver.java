package com.dommilosz.main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class minecraftserver implements Runnable {

	public static int PORT = 25565;
	public static Thread mcsvt;
	// Client Connection via Socket Class
	private final Socket connect;

	public minecraftserver(Socket c) {
		connect = c;
	}

	public static void main() {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			WriteLine("Listening for connections on port : " + PORT + " ...");

			// we listen until user halts server execution
			while (true) {
				minecraftserver myServer = new minecraftserver(serverConnect.accept());
				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				thread.start();
			}

		} catch (IOException e) {

		}
	}

	public static void startThread() {
		Runnable mssvtr = () -> minecraftserver.main();
		mcsvt = new Thread(mssvtr);
		mcsvt.start();
	}

	public static Thread.State GetThrState() {
		return mcsvt.getState();
	}

	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null;
		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
		String fileRequested = null;
	}
}
