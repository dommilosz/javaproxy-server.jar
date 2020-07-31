package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.configmanager;

import static com.dommilosz.utility.iowriter.log.WriteLine;

public class configcommand {
	public static String name = "config";

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		WriteLine("Config:");
		for (String line : configmanager.config) {
			WriteLine(line);
		}
		commandhandler.Executed = true;
	}
}
