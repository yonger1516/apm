package com.enniu.qa.apm.model;

/**
 * Created by fuyong on 2/18/16.
 */
public enum HostType {
	GENERATOR(0),
	SERVER(1);

	private int value;
	HostType(int value){
		this.value=value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
