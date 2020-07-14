package com.dommilosz.utility;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;

public class env {
	public static String path() {
		return System.getProperty("user.dir");
	}

	public static String user() {
		return System.getProperty("user.name");
	}

	public static String pcname() {
		return System.getenv("COMPUTERNAME");
	}

	public static long memorySize() {
		return ((OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
	}

	public static float memorySizeKB() {
		return memorySize() / 1024f;
	}

	public static float memorySizeMB() {
		return memorySizeKB() / 1024f;
	}

	public static float memorySizeGB() {
		return memorySizeMB() / 1024f;
	}

	public static long freeSpace(String path) {
		return new File(path).getTotalSpace();
	}

	public static float freeSpaceKB(String path) {
		return freeSpace(path) / 1024f;
	}

	public static float freeSpaceMB(String path) {
		return freeSpaceKB(path) / 1024f;
	}

	public static float freeSpaceGB(String path) {
		return freeSpaceMB(path) / 1024f;
	}

	public static String cpuID() {
		return System.getenv("PROCESSOR_IDENTIFIER");
	}

	public static String cpuX() {
		return System.getenv("NUMBER_OF_PROCESSORS");
	}

	public static String cpuArchW() {
		return System.getenv("PROCESSOR_ARCHITEW6432");
	}

	public static String cpuArch() {
		return System.getenv("PROCESSOR_ARCHITECTURE");
	}

	public static String osName() {
		return System.getProperty("os.name");
	}

	public static String osArch() {
		return System.getProperty("os.arch");
	}

	public static String osVersion() {
		return System.getProperty("os.version");
	}

	public static long unixTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public static boolean isWindows() {
		boolean isWindows = osName().toLowerCase().startsWith("windows");
		return isWindows;
	}
}
