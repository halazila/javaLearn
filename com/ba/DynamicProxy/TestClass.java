package com.ba.DynamicProxy;

import java.lang.reflect.Proxy;

public class TestClass {

	public static void main(String[] args) {
		Flyable sparrow = new Sparrow();
		FlyableInvocationHandler handler = new FlyableInvocationHandler(sparrow);

		Flyable bird = (Flyable) Proxy.newProxyInstance(sparrow.getClass().getClassLoader(),
				sparrow.getClass().getInterfaces(), handler);
		bird.fly();
	}

}
