package com.ba;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class TestEventQueue {

	private final int max;

	static class Event {

	}

	private final LinkedList<Event> eventQueue = new LinkedList<>();
	private final static int DEFAULT_MAX_EVENT = 10;

	public TestEventQueue(int max) {
		this.max = max;
	}

	public TestEventQueue() {
		this(DEFAULT_MAX_EVENT);
	}

	public void offer(Event event) {
		synchronized (eventQueue) {
			/*
			 * 用while不用if，防止多个线程同时进入wait，当某一个线程被唤醒后并将queueQueue装满，然后调用notifyAll将另一个在wait处阻塞的线程唤醒，
			 * 如果是用if，则被唤醒的线程会跳出if，执行下面的addLast，从而导致eventQueue的size超出max限制
			 */
			while (eventQueue.size() >= max) {
				try {
					out.println(Thread.currentThread().getName() + ": the queue is full");
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			eventQueue.addLast(event);
			out.println(Thread.currentThread().getName() + ": the new event is submitted." + " the eventQueue size="
					+ eventQueue.size());
			eventQueue.notifyAll();
		}
	}

	public Event take() {
		synchronized (eventQueue) {
			while (eventQueue.isEmpty()) {
				try {
					out.println(Thread.currentThread().getName() + ": the queue is empty");
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Event event = eventQueue.removeFirst();
			out.println(Thread.currentThread().getName() + ": the event " + event + " is handled."
					+ " the eventQueue size=" + eventQueue.size());
			eventQueue.notifyAll();

			return event;
		}
	}

	public static void main(String[] args) {
		final TestEventQueue testEventQueue = new TestEventQueue();

		new Thread(() -> {
			for (;;) {
				testEventQueue.offer(new TestEventQueue.Event());
			}
		}, "Producer1").start();
		new Thread(() -> {
			for (;;) {
				testEventQueue.take();
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "Consumer1").start();
		new Thread(() -> {
			for (;;) {
				testEventQueue.offer(new TestEventQueue.Event());
			}
		}, "Producer2").start();
		new Thread(() -> {
			for (;;) {
				testEventQueue.take();
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "Consumer2").start();

		// // 测试wait 和 synchronized阻塞能否成功
		// Thread t1 = new Thread(() -> {
		// for (;;) {
		// testEventQueue.offer(new TestEventQueue.Event());
		// }
		// }, "Producer1");
		// t1.start();
		// try {
		// TimeUnit.MILLISECONDS.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// t1.interrupt();// 中断wait方法的阻塞，成功
		// t1.interrupt();// 试图中断synchronized的阻塞，失败
	}

}
