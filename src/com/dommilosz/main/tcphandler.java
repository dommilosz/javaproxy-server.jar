package com.dommilosz.main;

import com.dommilosz.utility.arrayutil;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class tcphandler {
	public static int socketPort(){
		String type = tcpType();
		if(type.equals("server")){
			return tcpserver.socketport;
		}
		if(type.equals("client")){
			return tcpclient.socketport;
		}
		return 0;
	}
	public static void tcpStop(){
		try{
		tcpclient.clientStop();
		tcpserver.serverStop();
		}catch (Exception ex) {}
	}
	public static void startClient(String host,int port){
		tcpStop();
		tcpclient.start(host,port);
	}
	public static void startServer(int port,String pass){
		tcpStop();
		tcpserver.start(port,pass);
	}
	public static String tcpType(){
		boolean server = false;
		boolean client = false;
		if(tcpserver.serverSocket!=null&&!tcpserver.serverSocket.isClosed()){
			server = true;
		}
		if(tcpclient.socket!=null&&!tcpclient.socket.isClosed()){
			client = true;
		}
		if(server&&client){
			return "both! It shouldn't be!";
		}
		if(server){
			return "server";
		}
		if(client){
			return "client";
		}
		return "none";
	}

	public static class pktype {
		public static String any = "95PTZSCJHD";
		public static String cmd = "95PTZSCJHD#CMD";
		public static String raw = "95PTZSCJHD#RAW";
		public static String rawerr = "95PTZSCJHD#RAWERR";
		public static String ka = "95PTZSCJHD#KEEPALIVE";
		public static String authinfo = "95PTZSCJHD#AUTH";
		public static String authpass = "95PTZSCJHD#AUTHPASS";
		public static String userinfo = "95PTZSCJHD#USERINFO";
	}

	public static class socketConnection {
		public static List<socketConnection> sockets = new ArrayList<socketConnection>();
		public Socket socket;
		public boolean auth = false;
		public String hostname;
		public int invalidPassAttempts = 0;
		public String username = "";
		public String pcname = "";
		public int id = -1;

		public socketConnection(Socket s) {
			socket = s;
			sockets.add(this);
			hostname = socket.getInetAddress().getHostName();
			Random rnd = new Random();
			while (id<10000||id>99999){
				id = rnd.nextInt(99999);
			}

		}
		public static socketConnection getById(int id){
			for (socketConnection s:sockets){
				if(s.id==id)return s;
			}
			return null;
		}
	}

	public static class packet{
		String type;
		String content;
		public packet(String pk){
			String[] pka = pk.split(" ");
			type = pka[0];
			content = String.join(" ",arrayutil.rem(pka,0));
		}
		public static String createPacket(String content,String pktype){
			return pktype + " " + content;
		}
		public boolean checkType(String pktype){
			if(type.equals(pktype))return true;
			return false;
		}
	}
}

