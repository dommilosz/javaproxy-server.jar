package com.dommilosz.main;

import com.dommilosz.internalcommands.*;
import com.dommilosz.utility.array;

import static com.dommilosz.main.runner.runScript;
import static com.dommilosz.utility.logger.log.WriteLine;

public class commandhandler {
	public static boolean Executed = false;

	public static boolean RunInternalCommands(String cmd) {
		Executed = false;
		String[] args = cmd.split(" ");
		while (args.length < 8) {
			args = array.add(args, "");
		}
		remotecommand.execute(args);
		if (Executed) return true;
		redirecttoserver.execute(args);
		if (Executed) return true;

		stopcommand.execute(args);
		servercommand.execute(args);
		argscommand.execute(args);
		variablecommand.execute(args);

		return Executed;
	}

	public static void Exec(String cmd) {
		WriteLine("> " + cmd);
		if (!commandhandler.RunInternalCommands(cmd)) {
			runScript(cmd);
		}
	}

}
