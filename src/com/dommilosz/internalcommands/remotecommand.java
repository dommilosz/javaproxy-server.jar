package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.javarun;
import com.dommilosz.main.tcphandler;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class remotecommand {
	public static String name = "remote";

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		commandhandler.Executed = true;

		if (args[1].equals("")) {
			WriteLine("Usage | remote start <port> | remote connect <host> <port> | remote status |");
			return;
		}
		if (args[1].equals("status")) {
			WriteLine("Socket Type: " + tcphandler.tcpType());
			if (!tcphandler.tcpType().equals("none")) {
				WriteLine("Socket Port: " + tcphandler.socketPort());
			} else {
				WriteLine("Socket Port: " + "closed");
			}

			return;
		}
		if (args[1].equals("stop")) {
			WriteLine("Stopping Socket Type: " + tcphandler.tcpType());
			tcphandler.tcpStop();
			WriteLine("Stopped Socket");
			return;
		}
		if (args[2].equals("")) {
			WriteLine("Usage | remote start <port> | remote connect <host> <port> | remote status |");
			return;
		}
		if (args[1].equals("start")) {
			tcphandler.startServer(Integer.parseInt(args[2]));
		}
		if (args[1].equals("connect")) {
			if (args[3].equals("")) {
				WriteLine("Usage | remote start <port> | remote connect <host> <port> | remote status |");
				return;
			}
			tcphandler.startClient(args[2], Integer.parseInt(args[3]));
		}
	}
}
