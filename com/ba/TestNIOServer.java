package com.ba;

import static java.lang.System.out;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestNIOServer {

	private Selector selector;
	private ByteBuffer readBuff = ByteBuffer.allocate(1024);
	private ByteBuffer sendBuff = ByteBuffer.allocate(1024);

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
				else if (key.isWritable())
					write(key);
				itr.remove();
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		out.println("read from client...");
		this.readBuff.clear();
		int numRead;
		try {
			numRead = channel.read(readBuff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			key.cancel();
			channel.close();
			return;
		}
		String str = new String(readBuff.array(), 0, numRead);
		out.print(str);
		channel.register(selector, SelectionKey.OP_WRITE);
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		out.println("write to client...");
		sendBuff.clear();
		sendBuff.put("message from server".getBytes());
		sendBuff.flip();
		channel.write(sendBuff);
		channel.close();
		// channel.register(selector, SelectionKey.OP_READ);
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = ssc.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		out.println("a new client connected " + socketChannel.getRemoteAddress());
	}

	public static void main(String[] args) throws IOException {
		out.println("server started...");
		new TestNIOServer().start();
	}

}
