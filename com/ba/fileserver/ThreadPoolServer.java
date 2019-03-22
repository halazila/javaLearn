package com.ba.fileserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {
	private Selector selector;
	private ExecutorService executor = Executors.newFixedThreadPool(4);

	static class FileTask implements Runnable {

		private String fileName;
		private ByteBuffer buff;
		private int totalRecv;

		public FileTask(String fileName, ByteBuffer buff, int totalRecv) {
			this.fileName = fileName;
			this.buff = buff;
			this.totalRecv = totalRecv;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			File file = new File(fileName);
			OutputStream out = null;
			try {
				out = new FileOutputStream(file, true);
				out.write(buff.array(), 0, totalRecv);
				out.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}

	}

	public void start() throws IOException {
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

	public void read(SelectionKey key) throws IOException {
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

		String fileName = "./clients/" + strb;
		FileTask task = new FileTask(fileName, buff, totalRecv);
		executor.submit(task);
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

	public static void main(String[] args) throws IOException {
		System.out.println("server started");
		new ThreadPoolServer().start();
	}
}
