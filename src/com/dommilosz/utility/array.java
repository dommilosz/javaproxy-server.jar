package com.dommilosz.utility;

public class array {
	public static String[] addStr(String[] arr, String x) {
		int i;
		int n = arr.length;
		// create a new array of size n+1
		String[] newarr = new String[n + 1];

		// insert the elements from
		// the old array into the new array
		// insert all elements till n
		// then insert x at n+1
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	}
	public static Thread[] addThread(Thread[] arr, Thread x) {
		int i;
		int n = arr.length;
		// create a new array of size n+1
		Thread[] newarr = new Thread[n + 1];

		// insert the elements from
		// the old array into the new array
		// insert all elements till n
		// then insert x at n+1
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	}

	public static int[] addInt(int[] arr, int x) {
		int i;
		int n = arr.length;
		// create a new array of size n+1
		int[] newarr = new int[n + 1];

		// insert the elements from
		// the old array into the new array
		// insert all elements till n
		// then insert x at n+1
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	}

	public static String[] remStr(String[] arr, int index) {
		if (arr == null
				|| index < 0
				|| index >= arr.length) {

			return arr;
		}
		String[] anotherArray = new String[arr.length - 1];
		for (int i = 0, k = 0; i < arr.length; i++) {

			// if the index is
			// the removal element index
			if (i == index) {
				continue;
			}

			// if the index is not
			// the removal element index
			anotherArray[k++] = arr[i];
		}

		// return the resultant array
		return anotherArray;
	}

	public static int ArrindexOf(String[] arr, String el) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(el)) return i;
		}
		return -1;
	}
}
