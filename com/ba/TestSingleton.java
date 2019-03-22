package com.ba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.lang.System.out;

public class TestSingleton {

	private static volatile TestSingleton testSingleton;
	private String name;

	private TestSingleton() {
	};

	private TestSingleton copyFrom(TestSingleton source) {
		TestSingleton ret = new TestSingleton();
		ret.name = source.name;
		return ret;
	}

	public static TestSingleton getInstance() {
		if (testSingleton == null) {
			synchronized (TestSingleton.class) {
				if (testSingleton == null)
					testSingleton = new TestSingleton();
			}
		}
		return testSingleton;
	}

	public static void main(String[] args) {
		/*
		 * List<String> strList = new ArrayList<String>(Arrays.asList("a", "b", "c",
		 * "d", "e")); Iterator itr = strList.iterator(); while (itr.hasNext()) { String
		 * s = (String) itr.next(); if (s.equals("a")) strList.remove(0); }
		 */
		Map<String, Integer> map = new HashMap<>();
		TestSingleton test = new TestSingleton();
		test.name = "name";
		TestSingleton copy = test.copyFrom(test);
		out.println(copy.name);
		int[] intarr;
		intarr = new int[10];
		int[] intarr2 = new int[20];
		intarr2[10] = 5;
		intarr = intarr2;
		out.println(intarr[10]);
		int nfor = 55;
		nfor--;
		nfor |= nfor >>> 1;
		nfor |= nfor >>> 2;
		nfor |= nfor >>> 4;
		nfor |= nfor >>> 8;
		nfor |= nfor >>> 16;
		nfor++;
		out.println(nfor);

		System.out.println(0xff >>> 7);

		System.out.println((((byte) 0xff) >>> 7));

		System.out.println((byte) (((byte) 0xff) >>> 7));
		
	}

}
