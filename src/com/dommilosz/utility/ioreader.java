package com.dommilosz.utility;

import java.io.InputStream;
import java.util.Scanner;

import static com.dommilosz.utility.env.unixTime;
import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class ioreader {
	public static String readLine(InputStream s) throws Exception {
		String cmd = "";
		try {
			int ch = s.read();
			cmd += (char) ch;
			StringBuilder cmdBuilder = new StringBuilder(cmd);
			while (ch != 10 && ch >= 0) {
				ch = s.read();
				if (ch == 10 || ch < 0) break;
				cmdBuilder.append((char) ch);
			}
			cmd = cmdBuilder.toString();
		} catch (Exception ex) {
			throw new Exception("Input was empty!");
		}
		return cmd;
	}
}
