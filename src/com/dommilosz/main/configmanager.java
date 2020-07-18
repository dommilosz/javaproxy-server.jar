package com.dommilosz.main;

import com.dommilosz.utility.arrayutil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static com.dommilosz.utility.logger.log.WriteLine;

public class configmanager {
	public static String[] config = new String[]{
			"console.mode=terminal",
			"remote.autorun.enabled=false",
			"remote.autorun.port=25575",
			"remote.autorun.password=root",
			"minecraft.server.enabled=true",
			"minecraft.server.mode=simple",
			"minecraft.server.port=25565"
	};
	public static void loadFromFile(){
		try {
			FileReader fr = new FileReader("config.jdc");
			Scanner s = new Scanner(fr);
			while (s.hasNextLine()){
				String line = s.nextLine();
				String[] linea = line.split("=");
				setProperty(linea[0],linea[1]);
			}
			s.close();
		} catch (FileNotFoundException e) {
			try {
				FileWriter wr = new FileWriter("config.jdc");
				wr.write(String.join("\n",config));
				wr.flush();
				wr.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private static void setProperty(String property, String value) {
		int i = hasProperty(property);
		if(i>=0){
			config[i] = property+"="+value;
			return;
		}
		config = arrayutil.add(config,property+"="+value);
	}
	private static int hasProperty(String property) {
		for (int i = 0; i <config.length; i++) {
			if(config[i].split("=")[0].equals(property)){
				return i;
			}
		}
		return -1;
	}
	public static String getValue(String property){
		int i = hasProperty(property);
		String value = "";
		if(i>=0){
			value = config[i].split("=")[1];
		}
		if(value.equals("")){
			WriteLine("Value of %s was empty",property);
		}
		return value;
	}
}
