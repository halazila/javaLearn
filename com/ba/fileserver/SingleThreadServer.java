package com.ba.fileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleThreadServer {

	private Selector selector;

	public void start() throws IOException, InterruptedException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress("localhost", 9090));
		selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> itr = keys.iterator();
			while (itr.hasNext()) {
				SelectionKey key = itr.next();
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

	public void read(SelectionKey key) throws IOException, InterruptedException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buff = ByteBuffer.allocate(1024);
		int numRead, totalRecv = 0;
		while ((numRead = channel.read(buff)) > 0)
			totalRecv += numRead;
		String remoteAddr = channel.getRemoteAddress().toString();
		String[] tmp = remoteAddr.split("[\\/.:]");
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < tmp.length; i++) {
			strb.append(tmp[i]);
		}
		File file = new File("./clients/" + strb);
		OutputStream out = new FileOutputStream(file, true);
		out.write(buff.array(), 0, totalRecv);
		out.flush();
		out.close();
		//Thread.sleep(1000);
		System.out.println("recv from client :" + new String(buff.array(), 0, totalRecv));
		buff.flip();
		while (buff.hasRemaining())
			channel.write(buff);
		channel.close();
	}

	public void write(SelectionKey key) {

	}

	public void accept(SelectionKey key) throws IOException {
		ServerSocketChannel sc = (ServerSocketChannel) key.channel();
		SocketChannel channel = sc.accept();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
		return;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("server started");
		new SingleThreadServer().start();
	}
}
