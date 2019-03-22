package com.ba.fileserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Client implements Runnable {

	private static CountDownLatch begin = new CountDownLatch(1);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			begin.await();
			start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void start() throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(true);

		if (!channel.connect(new InetSocketAddress("127.0.0.1", 9090))) {
			while (!channel.finishConnect())
				//out.print(".");
				;
		}
		//out.println("\n");
		SocketAddress localAddr = channel.getLocalAddress();
		String message = "message from client " + localAddr.toString();
		ByteBuffer wbuf = ByteBuffer.allocate(1024);
		ByteBuffer rbuf = ByteBuffer.allocate(1024);
		wbuf.put(message.getBytes());
		wbuf.flip();
		while (wbuf.hasRemaining())
			channel.write(wbuf);
		int numRead, totalrecv = 0;

		while ((numRead = channel.read(rbuf)) > 0)
			totalrecv += numRead;
		out.println(new String(rbuf.array(), 0, totalrecv));
		channel.close();
	}

	public static void main(String[] args) throws InterruptedException {
		Thread[] thread = new Thread[100];
		long start = System.currentTimeMillis();
		for (int i = 0; i < thread.length; i++) {
			thread[i] = new Thread(new Client());
			thread[i].start();
		}
		begin.countDown();
		for (int i = 0; i < thread.length; i++)
			thread[i].join();
		long end = System.currentTimeMillis();
		out.println("time spent:" + (end - start) + "ms");
	}

}
