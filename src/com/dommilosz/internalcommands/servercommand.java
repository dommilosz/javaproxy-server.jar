package com.dommilosz.internalcommands;

import com.dommilosz.main.*;

import static com.dommilosz.utility.logger.log.WriteLine;

public class servercommand {
	public static String name = "server";

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		boolean running = true;
		if (minecraftserver.GetThrState() == Thread.State.TERMINATED) running = false;
		Thread mcsvt = minecraftserver.mcsvt;
		WriteLine("Server running: %s", running);
		WriteLine("Listening on %s", minecraftserver.PORT);
		WriteLine("ThreadId: %s", mcsvt.getId());
		commandhandler.Executed = true;
		minecraftserver.main();
	}
}
