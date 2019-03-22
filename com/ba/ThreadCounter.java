package com.ba;

import static java.lang.System.out;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCounter extends Thread {
	final static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		try {
			while (true) {
				new ThreadCounter().start();
			}
		} catch (Throwable e) {
			out.println("failed at => " + counter.get());
		}
	}

	@Override
	public void run() {
		try {
			out.println("the " + counter.getAndIncrement() + " thread be created");
			TimeUnit.MINUTES.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
