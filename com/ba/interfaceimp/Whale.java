package com.ba.interfaceimp;

public class Whale extends OceanAnimal implements Fish {

	//�ӿں͸���ͬʱʵ������ͬ�ķ������ڱ���������������ķ��������壬����ʹ�õ��Ǹ���ķ�����
	//������д>����̳�>�ӿ�ʵ��
	public static void main(String[] args) {
		Whale whale = new Whale();
		whale.swim();
	}

}
