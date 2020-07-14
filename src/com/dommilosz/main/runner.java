package com.dommilosz.main;

import com.dommilosz.utility.ioreader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.dommilosz.utility.env.*;
import static com.dommilosz.utility.logger.log.*;

public class runner {
	public static String mode = "terminal";

	public static String getInput() throws Exception {
		Scanner s = new Scanner(System.in);
		String cmd = ioreader.readLine(System.in);
		return cmd;
	}

	public static void runTerminal() {
		if (mode.equals("terminal")) {
			terminal.runTerminal(isWindows());
		}
	}

	public static void stopTerminal() {
		Thread stopthread = new Thread(() -> runScript("exit"));
		stopthread.start();
	}

	public static void runScript(String cmd) {
		if (mode.equals("terminal")) {
			try {
				terminal.runScript(cmd, isWindows());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				command.runScript(cmd, isWindows());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class command {
		public static void runScript(String cmd, boolean isWindows) throws IOException {
			String command;
			String arg1;
			if (isWindows) {
				command = ("cmd.exe");
				arg1 = "/c";
			} else {
				command = ("sh");
				arg1 = "-c";
			}
			ProcessBuilder pb = new ProcessBuilder()
					.command(command, arg1, cmd)
					.redirectErrorStream(true);
			Process process = pb.start();

			// enter code here
			log_Prefix = "";
			try {
				String line;
				Scanner sc = new Scanner(process.getInputStream());
				while ((line = ioreader.readLine(process.getInputStream())) != null) {
					Thread.sleep(20);
					WriteLine(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			log_Prefix = "";
		}
	}

	public static class terminal {
		public static Process terminal;
		public static Thread asyncreader;
		public static PrintWriter pWriter;
		public static boolean WriteToConsole = true;

		public static void runTerminalSync(boolean isWindows) throws IOException, InterruptedException {
			String command;
			if (isWindows) {
				command = ("cmd.exe");
			} else {
				command = ("sh");
			}
			ProcessBuilder pb = new ProcessBuilder()
					.command(command)
					.redirectErrorStream(true);
			terminal = pb.start();
		}

		public static void runTerminal(boolean isWindows) {
			Runnable rts = () -> {
				try {
					runTerminalSync(isWindows);
					runReader();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			};
			Thread terminalthread;
			terminalthread = new Thread(rts);
			terminalthread.start();
		}

		public static void runReader() throws IOException {
			Process cmd = terminal;

			final InputStream inStream = cmd.getInputStream();
			final InputStream errStream = cmd.getErrorStream();
			new Thread(() -> {
				while (true) {
					InputStreamReader reader = null;
					Scanner scan = new Scanner(inStream);
					while (scan.hasNextLine()) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						log_Prefix = "";
						try {
							WriteLine(ioreader.readLine(inStream));
						} catch (Exception e) {

						}

						log_Prefix = "#";
					}
				}
			}).start();
		}

		public static void runScript(String cmd, boolean isWindows) throws InterruptedException {
			OutputStream outStream = terminal.getOutputStream();
			PrintWriter pWriter = new PrintWriter(outStream);
			pWriter.println(cmd);
			if (isWindows) {
				pWriter.println("");
			} else {
				pWriter.println(String.format("echo %s@%s:~$(pwd)", user(), pcname()));
			}
			pWriter.flush();

		}
	}
}
