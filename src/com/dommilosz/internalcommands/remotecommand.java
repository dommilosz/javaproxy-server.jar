package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.main.javarun;
import com.dommilosz.main.tcphandler;
import com.dommilosz.main.tcpserver;
import com.dommilosz.utility.arrayutil;

import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class remotecommand {
	public static void writeUsage(){
		WriteLine("Usage | remote start <port> <(optional)password> | remote connect <host> <port> | remote status | remote socketlist | remote kick <id> <reason> |");
	}
	public static String name = "remote";

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		commandhandler.Executed = true;

		if (args[1].equals("")) {
			writeUsage();
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
		if (args[1].equals("socketlist")) {
			if(tcphandler.tcpType().equals("server")){
				WriteLine("Server Sockets List:");
				for (tcphandler.socketConnection sc:tcphandler.socketConnection.sockets){
					String hostname = String.valueOf(sc.socket.getInetAddress().getHostName());
					WriteLine("%s (%s) - %s@%s",hostname,sc.id,sc.username,sc.pcname);
				}
			}else {
				log_level =21;
				WriteLine("This command is enabled only in server mode");
			}

			return;
		}
		if (args[1].equals("kick")) {
			if(args.length<4)writeUsage();
			if(args[2].equals(""))writeUsage();
			if(args[3].equals(""))writeUsage();
			if(tcphandler.tcpType().equals("server")){
				try{
					tcphandler.socketConnection sc = tcphandler.socketConnection.getById(Integer.parseInt(args[2]));
					if(sc==null){
						log_level = 21;
						WriteLine("There is no client with provided id!");
						return;
					}
					String[] resarr = arrayutil.rem(args,0);
					resarr = arrayutil.rem(resarr,0);
					resarr = arrayutil.rem(resarr,0);
					resarr = arrayutil.rem(resarr,0);
					String reason = String.join(" ",resarr);
					tcpserver.kickSocket(sc,reason);
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}else {
				log_level =21;
				WriteLine("This command is enabled only in server mode");
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
			writeUsage();
			return;
		}
		if (args[1].equals("start")) {
			tcphandler.startServer(Integer.parseInt(args[2]),args[3]);
		}
		if (args[1].equals("connect")) {
			if (args[3].equals("")) {
				writeUsage();
				return;
			}
			tcphandler.startClient(args[2], Integer.parseInt(args[3]));
		}
	}
}
