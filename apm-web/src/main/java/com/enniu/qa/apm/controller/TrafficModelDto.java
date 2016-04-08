package com.enniu.qa.apm.controller;

import com.enniu.qa.apm.model.TimeUnitEnum;

/**
 * Created by fuyong on 7/20/15.
 */
public class TrafficModelDto {

	private long apiId;
	private String name;
	private double rate;
	private TimeUnitEnum unit;

	public long getApiId() {
		return apiId;
	}

	public void setApiId(long apiId) {
		this.apiId = apiId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public TimeUnitEnum getUnit() {
		return unit;
	}

	public void setUnit(TimeUnitEnum unit) {
		this.unit = unit;
	}
}
