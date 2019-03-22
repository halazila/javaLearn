package com.ba.nettyLearn.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {

	/**
	 * 默认的序列号
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private int userID;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public UserInfo buildUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public UserInfo buildUserID(int userID) {
		this.userID = userID;
		return this;
	}

	/*
	 * 自定义编码方法
	 */
	public byte[] codeC() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}

	public static void main(String[] args) throws IOException {
		UserInfo info = new UserInfo();
		info.buildUserID(100).buildUserName("Hey, What's up");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		byte[] b = bos.toByteArray();
		System.out.println("jdk serializable lethen is :" + b.length);// jdk 序列化字节长度
		bos.close();
		System.out.println("codeC serializable lenth is :" + info.codeC().length);// codeC方法序列化字节长度

		// 性能测试
		int loop = 1000000;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(info);
			oos.flush();
			oos.close();
			byte[] barray = baos.toByteArray();
			baos.close();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("jdk serialize spend time :" + (endTime - startTime) + "ms");

		startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			byte[] bs = info.codeC();
		}
		endTime = System.currentTimeMillis();
		System.out.println("codeC serialize spend time :" + (endTime - startTime) + "ms");
	}

}
