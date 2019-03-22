package com.ba;

import static java.lang.System.out;

public class TestMultiThread {

	public static volatile boolean stop = false;

	public static void main(String[] args) throws InterruptedException {
		Thread testThread = new Thread() {

			@Override
			public void run() {
				int i = 1;
				while (!stop) {
					i++;
				}
				out.println("thread stop i=" + i);
			}
		};
		testThread.start();
		Thread.sleep(1000);
		stop = true;
		out.println("now stop is " + stop);
		testThread.join();
	}
}
