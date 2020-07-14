package com.dommilosz.internalcommands;

import com.dommilosz.main.javarun;
import com.dommilosz.main.commandhandler;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class stopcommand {
	public static String name = "stop";

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		commandhandler.Executed = true;
		javarun.wantExit = true;
		log_level = 21;
		WriteLine("Closing...");
	}
}
