package com.ba;

import java.util.concurrent.TimeUnit;

public class TestMutex {

	private final static Object mutex = new Object();

	public void accessResource() {
		synchronized (mutex) {
			try {
				TimeUnit.MINUTES.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		final TestMutex testMutex = new TestMutex();
		for (int i = 0; i < 5; i++)
//			new Thread(testMutex::accessResource).start();//等价为以下匿名类写法
			new Thread() {
			@Override
			public void run() {
				testMutex.accessResource();
			}
			}.start();
	}

}
