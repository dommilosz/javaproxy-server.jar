package com.dommilosz.utility;


import com.dommilosz.main.tcphandler;
import com.dommilosz.main.tcphandler.pktype;

import java.time.LocalTime;

public class logger {
	public static class log {
		public static String log_Prefix = "#";
		public static int log_level = 1;
		public static boolean log_linebreak = true;
		public static String log_time = "#";
		public static String appname = "JDC";
		public static String prefix = "[" + appname + "]";
		public static String[] log = new String[]{""};
		public static String errorstr = "??xGVWnU028M??";

		public static void Info(String txt, boolean br) {
			log = arrayutil.add(log, txt);
			if (br) {
				System.out.println(txt);
			} else {
				System.out.print(txt);
			}

		}

		public static void Error(String txt, boolean br) {
			log = arrayutil.add(log, txt);
			if (br) {
				System.err.println(txt);
			} else {
				System.err.print(txt);
			}
		}

		public static void log(String txt, int level, boolean br) {
			try {
				tcphandler.tcpserver.writeClient(txt, pktype.raw);
			} catch (Exception ex) {
			}
			switch (level) {
				case 1:
					Info(txt, br);
					break;
				case 2:
					Error(txt, br);
					break;
				case 21: {
					Error(txt, br);
					log_level = 1;
					break;
				}
			}
		}

		public static void WriteLine(String txt, Object... args) {
			if (args.length > 0) {
				txt = String.format(txt, args);
			}

			int level = log_level;
			String timestr = GetTime();
			if (log_Prefix.equals("#")) {
				log_Prefix = prefix;
			}
			boolean usePrefix = !log_Prefix.equals("");
			boolean useTime = !timestr.equals("");
			String cmd = txt;
			if (usePrefix) txt = log_Prefix + " " + txt;
			if (useTime) txt = timestr + " " + txt;
			log(txt, level, log_linebreak);
		}

		public static String GetTime() {
			LocalTime time = LocalTime.now();
			if (log_time.equals("#")) {
				log_time = "[%s:%s:%s]";
			}
			if (log_time.equals("")) return "";
			String h = String.format("%s", time.getHour());
			String m = String.format("%s", time.getMinute());
			String s = String.format("%s", time.getSecond());
			if (h.length() < 2) h = "0" + h;
			if (m.length() < 2) m = "0" + m;
			if (s.length() < 2) s = "0" + s;
			return String.format(log_time, h, m, s);
		}
	}
}
