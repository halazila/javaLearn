package com.ba;

import static java.lang.System.out;

public class TestSynchronize {

	//�̻߳�ȡ����������ͬʱֻ����һ���̳߳��У������ǲ�ͬ���߳�ִ�в�ͬ�Ĵ����
	synchronized public static void sync1() {
		out.println("synchronize static method 1 begin");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("synchronize static method 1 end");
	}
	//�̻߳�ȡ������������ͬʱֻ����һ���̳߳����������²�ͬ���߳�ִ�в�ͬ�Ĵ����
	synchronized public void sync2() {
		out.println("synchronize non-static method 1 begin");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("synchronize non-static method 1 end");
	}

	synchronized public static void sync3() {
		out.println("synchronize static method 2 begin");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("synchronize static method 2 end");
	}

	synchronized public void sync4() {
		out.println("synchronize non-static method 2 begin");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("synchronize non-static method 2 end");
	}

	public static void main(String[] args) {
		TestSynchronize test = new TestSynchronize();
		new Thread(new Runnable() {
			public void run() {
				test.sync2();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				test.sync4();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				TestSynchronize.sync1();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				TestSynchronize.sync3();
			}
		}).start();

	}

}
