package com.ba.interfaceimp;

public class Whale extends OceanAnimal implements Fish {

	//接口和父类同时实现了相同的方法，在编译器看来，父类的方法更具体，子类使用的是父类的方法。
	//子类重写>父类继承>接口实现
	public static void main(String[] args) {
		Whale whale = new Whale();
		whale.swim();
	}

}
