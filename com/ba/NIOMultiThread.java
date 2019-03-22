package com.ba;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import static java.lang.System.out;

public class NIOMultiThread {

	private Selector selector;
	private ByteBuffer readBuf = ByteBuffer.allocate(1024);
	private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

	public void start() throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress("localhost", 8001));
		selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		while (!Thread.currentThread().interrupted()) {
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> itr = keys.iterator();
			while (itr.hasNext()) {
				SelectionKey key = itr.next();
				if (!key.isValid())
					continue;
				if (key.isAcceptable())
					accept(key);
				else if (key.isReadable())
					read(key);
				// else if (key.isWritable())
				// write(key);
			}
			keys.clear();
		}
	}

	private void read(SelectionKey key) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		SocketChannel channel = (SocketChannel) key.channel();
		int numRead;
		try {
			numRead = channel.read(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			key.cancel();
			channel.close();
			return;
		}
		if (numRead <= 0) {
			channel.close();
			return;
		}
		String str = new String(buf.array(), 0, numRead);
		out.println("read from client-" + str);
		for (int i = 0; i < 100000000; i++) {
			int tmp = i / 10;
		}
		buf.flip();
		channel.write(buf);
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		out.println("write to client...");
		writeBuf.clear();
		writeBuf.put("message from server".getBytes());
		writeBuf.flip();
		channel.write(writeBuf);
		channel.close();
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel sc = (ServerSocketChannel) key.channel();
		SocketChannel channel = sc.accept();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
		out.println("new client connected " + channel.getRemoteAddress());
	}

	public static void main(String[] args) {
		out.println("server started");
		try {
			new NIOMultiThread().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
