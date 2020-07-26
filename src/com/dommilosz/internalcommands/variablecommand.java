package com.dommilosz.internalcommands;

import com.dommilosz.main.commandhandler;
import com.dommilosz.utility.arrayutil;
import com.dommilosz.utility.logger;


import static com.dommilosz.utility.logger.log.WriteLine;
import static com.dommilosz.utility.logger.log.log_level;

public class variablecommand {
	public static String eqls = "=#??QAATUYGbgt#=";
	public static String errs = "=#??o0AmU3GFPo#=";
	public static String name = "var";
	public static String[] vars = new String[]{"appname" + eqls + logger.log.appname};

	public static void writeUsage() {
		WriteLine("Usage | var set <varname> <value> | var del <varname> | var get <varname> | var all");
	}

	public static void execute(String... args) {
		if (!args[0].equals(name)) return;
		commandhandler.Executed = true;

		if (args[1].equals("")) {
			writeUsage();
			return;
		}
		switch (args[1]) {
			case "set": {
				if (args[3].equals("")) {
					writeUsage();
					return;
				}
				if (args[2].equals("")) {
					writeUsage();
					return;
				}
				SetVar(args[2], args[3]);
				break;
			}
			case "reset":
			case "del": {
				SetVar(args[2], "null");
				break;
			}
			case "debug": {
				WriteLine("Debug vars display");
				for (String var : vars) {
					WriteLine(var);
				}
				break;
			}
			case "list":
			case "all": {
				WriteLine("Vars display");
				for (String var : vars) {
					String[] vara = SplitVar(var);
					WriteLine("\"%s\" == \"%s\"", vara[0], vara[1]);
				}
				break;
			}
			case "get": {
				String var = findVar(args[2]);
				String[] vara = SplitVar(var);
				if (var.equals(errs)) {
					WriteLine("\"%s\" == \"%s\"", args[2], "null");
				} else {
					WriteLine("\"%s\" == \"%s\"", vara[0], vara[1]);
				}
				break;
			}
			default:
				writeUsage();
		}
	}

	public static String findVar(String varname) {
		for (String var : vars) {
			if (SplitVar(var)[0].equals(varname)) {
				return var;
			}
		}
		return errs;
	}

	public static String[] SplitVar(String var) {
		int i = var.indexOf(eqls);
		if (i < 0) return new String[]{"null", "null"};
		String[] donevara = new String[]{var.substring(0, i), var.substring(i + eqls.length())};

		donevara[0] = donevara[0].trim();
		donevara[1] = donevara[1].trim();
		return donevara;
	}

	public static void SetVar(String varname, String value) {
		if (varname.contains(eqls) || varname.contains(errs) || value.contains(eqls) || value.contains(errs)) {
			log_level = 21;
			WriteLine("You tried to perform an illegal operation!");
			return;
		}
		String var = findVar(varname);

		if (var.equals(errs)) {
			vars = arrayutil.add(vars, varname + eqls + value);
		} else {
			int i = arrayutil.ArrindexOf(vars, var);
			vars[i] = varname + eqls + value;
		}
		var = findVar(varname);
		String[] vara = SplitVar(var);
		WriteLine("\"%s\" == \"%s\"", vara[0], vara[1]);
	}
}
