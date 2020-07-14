package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.javarun;
import com.dommilosz.main.tcphandler;

import java.io.IOException;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;
import static com.dommilosz.main.tcphandler.pktype;

public class redirecttoserver {
	public static void execute(String... args) {
		try {
			if (tcphandler.socketOpen() && tcphandler.sockettype().equals("client")) {
				commandhandler.Executed = true;
				try {
					tcphandler.tcpclient.writeServer(String.join(" ", args), pktype.cmd);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
		}

	}
}
