package com.enniu.qa.apm.model;

/**
 * Created by fuyong on 7/10/15.
 */
public enum CommitType {
	Jenkins(0),
	Mannual(1);

	private int code;

	private CommitType(int code){
		this.code=code;
	}
}
