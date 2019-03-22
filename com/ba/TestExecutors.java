package com.ba;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestExecutors {

	public static class RunnableTask implements Runnable {

		private String name;

		RunnableTask(String name) {
			super();
			this.name = name;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			out.println(Thread.currentThread().getName() + " execute runnable task " + name);
		}
	}

	public static class CallableTask implements Callable {

		private String name;

		CallableTask(String name) {
			super();
			this.name = name;
		}

		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			Thread.sleep(100);
			out.println(Thread.currentThread().getName() + " execute callable task " + name);
			return name;
		}

	}

	public static void main(String[] args) throws InterruptedException, Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		// runnable task test
		for (int i = 0; i < 10; i++) {
			executorService.submit(new RunnableTask("runnable-task-" + i));
		}

		// callable task test
		List<Future<String>> results = new ArrayList<Future<String>>();
		for (int i = 0; i < 10; i++) {
			results.add(executorService.submit(new CallableTask("callable-task-" + i)));
		}
		for (Future<String> s : results) {
			out.println(s.get());
		}

		// shutdown executorService
		if (!executorService.isShutdown())
			executorService.shutdown();

	}

}
