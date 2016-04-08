package com.enniu.qa.apm.model;

/**
 * Created by fuyong on 7/3/15.
 */
public enum ProjectType {
	TPS_DRIVEN(0),
	USER_DRIVEN(1),
	HYBRID(2);

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	private ProjectType(int code){
		this.code=code;
	}


}
