package com.ba;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.out;//��̬����

/*
 * Java �ؼ���ʹ��ʾ��
 */
public class KeyWordTest<T> {
	private String member1;

	/*
	 * null �Ƿ�ΪObject�����ж�
	 */
	static String nullObject() {
		if (null instanceof Object)
			return "null is a instance of Object";
		else
			return "null is not a instance of Object";
	}

	/*
	 * strictfp �ؼ���ʹ��ʾ��
	 */
	static strictfp <T> double multiply(T arg1, T arg2) {
		double x = (double) arg1;
		double y = (double) arg2;
		return x * y;
	}

	/*
	 * transient �ؼ���ʹ��ʾ��
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
		 * ��̬�ڲ��಻�ܷ����ⲿ��ĳ�Ա
		 */
		/*
		 * public void accOutclass() { System.out.println(member1);//compile error }
		 */

	}

	/*
	 * �ڲ���
	 */
	class InnerClass {
		/*
		 * �ڲ������ֱ�ӷ����ⲿ��ĳ�Ա��������˽�г�Ա
		 */
		public void accOutclass() {
			out.println(member1);
		}
	}
	/*
	 * final �ؼ���ʾ�������ܸı�final�ؼ������εı����������ܸı��������ֵ�������ǿ��Ըı����ָ��Ķ�������ݡ�
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
