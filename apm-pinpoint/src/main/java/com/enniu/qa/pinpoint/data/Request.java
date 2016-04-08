package com.enniu.qa.pinpoint.data;

/**
 * Created by fuyong on 2/16/16.
 */
public class Request {
	boolean satisfied;
	boolean tolerated;
	boolean frustrated;

	double rt;

	public double getRt() {
		return rt;
	}

	public void setRt(double rt) {
		this.rt = rt;
	}
}
