package com.ba.interfaceimp;

public interface Fish {

	default void swim() {
		System.out.println("swim method in interface Fish");
	}

}
