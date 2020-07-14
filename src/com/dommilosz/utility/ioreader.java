package com.dommilosz.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static com.dommilosz.utility.env.unixTime;
import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class ioreader {
	public static String readLine(Scanner s,String name) throws Exception {
		Task t = new Task(s,name);
		while (t.readed==null){
			Thread.sleep(150);
		}
		t.stop();
		return t.readed;
	}
	public static String readLine(Scanner s,String name,int mstimeout) throws Exception {
		Task t = new Task(s,name);
		long tbefore = unixTime();
		float timeout = mstimeout/1000f;
		while (t.readed==null){
			Thread.sleep(150);
			long tnow = unixTime();
			if(tnow-tbefore>timeout) {
				t.stop();
				log_level= 21;
				WriteLine("[IORead] >> Timed Out!");
				throw new Exception("Timed Out");
			}
		}
		t.stop();
		return t.readed;
	}
	public static class Task{
		public Scanner sc;
		public Thread thread;
		public String readed = null;
		public String name = "";
		public Task(Scanner s,String nm){
			sc = s;
			name = nm;
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						read();
					} catch (Exception e) {

					}
				}
			});
			thread.start();
		}
		public void read() throws Exception {
			if(sc.hasNextLine()){
				String line = sc.nextLine();
				readed = line;
			}
			throw new Exception("Input was empty");
		}
		public void stop(){
			try{ thread.stop();}catch (Exception ex){}
		}
	}
}
