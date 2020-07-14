package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.javarun;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class argscommand {
	public static String name = "args";
	public static String[] cmdargs;

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		WriteLine("CMD args. Length: %s", cmdargs.length);
		commandhandler.Executed = true;
		for (String arg : cmdargs) {
			WriteLine(arg);
		}
	}
}
