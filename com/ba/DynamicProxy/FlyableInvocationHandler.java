package com.ba.DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import static java.lang.System.out;

public class FlyableInvocationHandler implements InvocationHandler {

	private Object target;

	public FlyableInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		out.println("before method");
		Object rst = method.invoke(target, args);
		out.println("after method");
		return rst;
	}

}
