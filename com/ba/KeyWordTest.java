package com.ba;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.out;//静态导包

/*
 * Java 关键词使用示例
 */
public class KeyWordTest<T> {
	private String member1;

	/*
	 * null 是否为Object对象判断
	 */
	static String nullObject() {
		if (null instanceof Object)
			return "null is a instance of Object";
		else
			return "null is not a instance of Object";
	}

	/*
	 * strictfp 关键词使用示例
	 */
	static strictfp <T> double multiply(T arg1, T arg2) {
		double x = (double) arg1;
		double y = (double) arg2;
		return x * y;
	}

	/*
	 * transient 关键词使用示例
	 */
	static class TransientTest implements Serializable {
		private static final long serialVersionUID = -766571861519198043L;
		private String name;
		private transient String sex;

		public TransientTest(String name, String sex) {
			super();
			this.name = name;
			this.sex = sex;
		}

		@Override
		public String toString() {
			return "TransientTest[name=" + name + ",sex=" + sex + "]";
		}

		public static void serialize(Object obj) throws IOException, ClassNotFoundException {
			FileOutputStream fos = new FileOutputStream("obj.out");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			fos.close();
		}

		public static Object deserialize(String file) throws ClassNotFoundException, IOException {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return obj;
		}
		/*
		 * 静态内部类不能访问外部类的成员
		 */
		/*
		 * public void accOutclass() { System.out.println(member1);//compile error }
		 */

	}

	/*
	 * 内部类
	 */
	class InnerClass {
		/*
		 * 内部类可以直接访问外部类的成员，甚至是私有成员
		 */
		public void accOutclass() {
			out.println(member1);
		}
	}
	/*
	 * final 关键字示例，不能改变final关键字修饰的变量（即不能改变变量引用值），但是可以改变变量指向的对象的内容。
	 */
	static void finalTest(final String argv) {
		out.println(argv);
		//compile error, it's not ok, argv cannot be changed to reference a new String Object
		//argv = new String("halea");
		//it's ok
		argv.toLowerCase();
		out.println(argv);
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		out.println(nullObject());
		out.println(5.3221341 * 5.32321);
		out.println(multiply(5.3221341, 5.32321));
		TransientTest oriObj = new TransientTest("deli", "femal");
		TransientTest.serialize(oriObj);
		TransientTest deserialObj = (TransientTest) TransientTest.deserialize("obj.out");
		out.println(oriObj);
		out.println(deserialObj);
	}
}
