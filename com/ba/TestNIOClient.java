package com.ba;

import static java.lang.System.out;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestNIOClient implements Runnable {

	
	

	public void start() throws IOException {
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		sc.connect(new InetSocketAddress("localhost", 8001));
		Selector selector = Selector.open();
		sc.register(selector, SelectionKey.OP_CONNECT);
		while (true) {
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> itr = keys.iterator();
			while (itr.hasNext()) {
				SelectionKey key = itr.next();
				itr.remove();
				if (key.isConnectable()) {
					SocketChannel channel = (SocketChannel) key.channel();
					channel.finishConnect();
					channel.register(selector, SelectionKey.OP_WRITE);
					out.println("server connected");
				} else if (key.isWritable()) {
					String str = "message from client-" + Thread.currentThread().getName();
					ByteBuffer sendBuff = ByteBuffer.allocate(1024);
					sendBuff.clear();
					sendBuff.put(str.getBytes());
					sendBuff.flip();
					SocketChannel channel = (SocketChannel) key.channel();
					channel.write(sendBuff);
					channel.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					out.print("receive message from server");
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer readBuff = ByteBuffer.allocate(1024);
					readBuff.clear();
					int num = channel.read(readBuff);
					out.println(new String(readBuff.array(), 0, num));
					channel.close();
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10; i++)
			new Thread(new TestNIOClient()).start();
	}
}
