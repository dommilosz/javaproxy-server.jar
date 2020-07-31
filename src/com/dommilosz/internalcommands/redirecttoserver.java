package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.tcpclient;
import com.dommilosz.main.tcphandler;

import static com.dommilosz.main.tcpclient.writeServer;
import static com.dommilosz.main.tcphandler.pktype;

public class redirecttoserver {
	public static void execute(String... args) {
		try {
			if (tcphandler.tcpType().equals("client")) {
				commandhandler.Executed = true;
				try {
					if (tcpclient.passmode) {
						writeServer(String.join(" ", args), tcphandler.pktype.authpass);
					} else {
						writeServer(String.join(" ", args), pktype.cmd);
					}
				} catch (Exception ex) {
				}
			}
		} catch (Exception ex) {
		}

	}
}
