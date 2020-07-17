package com.dommilosz.main;

import java.io.*;
import java.util.Scanner;

import com.dommilosz.internalcommands.argscommand;

import static com.dommilosz.main.commandhandler.Exec;
import static com.dommilosz.main.runner.*;
import static com.dommilosz.main.runner.getInput;
import static com.dommilosz.utility.env.*;
import static com.dommilosz.utility.logger.log.*;

public class javarun {
	public static boolean wantExit = false;

	public static void main(String[] args) throws IOException, InterruptedException {
		argscommand.cmdargs = args;
		configmanager.loadFromFile();
		WriteLine("Starting " + appname, 1);
		WriteLine("");
		WriteLine("Running on:", 1);
		WriteLine("RAM  : %sB (%sGB)", memorySize(), memorySizeGB());
		WriteLine("CPU  : %s x %s %s %s", cpuID(), cpuX(), cpuArchW(), cpuArch());
		WriteLine("DISK : %sB (%sGB)", freeSpace("/"), freeSpaceGB("/"));
		WriteLine("USER : %s", user());
		WriteLine("OS   : %s (%s [%s])", osName(), osArch(), osVersion());
		WriteLine("");
		String system = "LINUX  ";
		if (isWindows()) system = "WINDOWS";
		String startscript = "start.sh ";
		if (isWindows()) startscript = "start.bat";
		runner.mode = configmanager.getValue("console.mode");
		runTerminal();
		File startscriptfile = new File(startscript);
		boolean exists = startscriptfile.exists();
		if (exists) {
			WriteLine("[%s] found!", startscript);
			WriteLine("Loading  [%s]", startscript);
			WriteLine("Starting [%s]", startscript);
			runScript(startscript);
			WriteLine("Executed [%s]", startscript);
		} else {
			WriteLine("[" + startscript + "] not found!");
		}
		int port = Integer.parseInt(configmanager.getValue("minecraft.server.port"));
		if(configmanager.getValue("minecraft.server.enabled").equals("true")){
			if(configmanager.getValue("minecraft.server.mode").equals("simple")){
				minecraftserver.PORT = port;
				WriteLine("Running listener on port %s", port);
				minecraftserver.startThread();
			}
		}
		if(configmanager.getValue("remote.autorun.enabled").equals("true")){
			int remport = Integer.parseInt(configmanager.getValue("remote.autorun.port"));
			tcpserver.start(remport);
		}

		WriteLine("==================================");
		WriteLine("| WELCOME TO dommilosz's console |");
		WriteLine("| Write command here [" + system + "]   |");
		WriteLine("==================================");
		WriteLine("Current DIR: [%s]", path());
		while (!wantExit) {
			try {
				String cmd = getInput();
				Exec(cmd);


			} catch (Exception err) {
				err.printStackTrace();
			}
		}

		System.exit(0);
	}


}