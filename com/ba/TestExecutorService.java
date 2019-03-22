package com.ba;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TestExecutorService {
	static ExecutorService e = Executors.newFixedThreadPool(2);
	static int N = 1000000;

	public static long doTest(final BlockingQueue<Integer> q, final int n) {
		long t = System.nanoTime();
		e.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int i = 0; i < n; ++i) {
					try {
						q.put(i);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		try {
			Long r = e.submit(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					long sum = 0;
					// TODO Auto-generated method stub
					for (int i = 0; i < n; ++i) {
						sum += q.take();
					}
					return sum;
				}
			}).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		t = System.nanoTime() - t;

		return (long) (1000000000.0 * N / t);

	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			int length = (i == 0) ? 1 : i * 5;
			System.out.print(length + "\t");
			System.out.print(doTest(new LinkedBlockingQueue<Integer>(length), N) + "\t");
			System.out.print(doTest(new ArrayBlockingQueue<Integer>(length), N) + "\t");
			System.out.print(doTest(new SynchronousQueue<Integer>(), N));
			System.out.println();
		}

		e.shutdown();
	}

}
