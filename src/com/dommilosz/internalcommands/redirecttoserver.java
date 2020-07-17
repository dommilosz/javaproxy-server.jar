package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.javarun;
import com.dommilosz.main.tcpclient;
import com.dommilosz.main.tcphandler;

import java.io.IOException;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;
import static com.dommilosz.main.tcphandler.pktype;

public class redirecttoserver {
	public static void execute(String... args) {
		try {
			if (tcphandler.tcpType().equals("client")) {
				commandhandler.Executed = true;
				try {
					tcpclient.writeServer(String.join(" ", args), pktype.cmd);
				} catch (Exception ex) {}
			}
		} catch (Exception ex) {
		}

	}
}
