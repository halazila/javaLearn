package com.ba.DynamicProxy;

import static java.lang.System.out;

public class Sparrow implements Flyable {

	@Override
	public void fly() {
		// TODO Auto-generated method stub
		out.println("sparrow fly...");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
