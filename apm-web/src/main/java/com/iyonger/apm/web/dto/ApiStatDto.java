package com.iyonger.apm.web.dto;

/**
 * Created by fuyong on 7/19/15.
 */
public class ApiStatDto {
	private long id;
	private String name;
	private String description;
	private double avgApdex;
	private double avgTPS;
	private double avgRT;
	private double sdtDev;
	private int runNum;

	public int getRunNum() {
		return runNum;
	}

	public void setRunNum(int runNum) {
		this.runNum = runNum;
	}

	public double getAvgApdex() {
		return avgApdex;
	}

	public void setAvgApdex(double avgApdex) {
		this.avgApdex = avgApdex;
	}

	public double getAvgTPS() {
		return avgTPS;
	}

	public void setAvgTPS(double avgTPS) {
		this.avgTPS = avgTPS;
	}

	public double getAvgRT() {
		return avgRT;
	}

	public void setAvgRT(double avgRT) {
		this.avgRT = avgRT;
	}

	public double getSdtDev() {
		return sdtDev;
	}

	public void setSdtDev(double sdtDev) {
		this.sdtDev = sdtDev;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
