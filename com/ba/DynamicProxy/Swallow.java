package com.ba.DynamicProxy;

import static java.lang.System.out;
public class Swallow implements Flyable{

	@Override
	public void fly() {
		// TODO Auto-generated method stub
		out.println("swallow fly...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
