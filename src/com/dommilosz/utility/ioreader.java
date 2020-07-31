package com.dommilosz.utility;

import java.io.InputStream;

public class ioreader {
	public static String readLine(InputStream s) throws Exception {
		String cmd = "";
		try {
			int ch = s.read();
			cmd += (char) ch;
			StringBuilder cmdBuilder = new StringBuilder(cmd);
			while (ch != 10 && ch >= 0) {
				ch = s.read();
				if (ch < 0) throw new Exception();
				if (ch == 10 || ch < 0) break;
				if (ch != 13)
					cmdBuilder.append((char) ch);
			}
			if (ch < 0) throw new Exception();
			cmd = cmdBuilder.toString();
		} catch (Exception ex) {
			throw new Exception("Input was empty!");
		}
		return cmd;
	}
}
