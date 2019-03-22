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
			 * ��while����if����ֹ����߳�ͬʱ����wait����ĳһ���̱߳����Ѻ󲢽�queueQueueװ����Ȼ�����notifyAll����һ����wait���������̻߳��ѣ�
			 * �������if���򱻻��ѵ��̻߳�����if��ִ�������addLast���Ӷ�����eventQueue��size����max����
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

		// // ����wait �� synchronized�����ܷ�ɹ�
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
		// t1.interrupt();// �ж�wait�������������ɹ�
		// t1.interrupt();// ��ͼ�ж�synchronized��������ʧ��
	}

}
