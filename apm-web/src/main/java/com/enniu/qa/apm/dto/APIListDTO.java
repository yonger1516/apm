package com.enniu.qa.apm.dto;

/**
 * Created by fuyong on 7/17/15.
 */
public class APIListDTO {
	private long id;
	private String name;
	private String description;
	private  double avgRT;
	private double sdtDev;
	private double avgApdex;
	private double avgTps;
	private int runNum;

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

	public double getAvgApdex() {
		return avgApdex;
	}

	public void setAvgApdex(double avgApdex) {
		this.avgApdex = avgApdex;
	}

	public double getAvgTps() {
		return avgTps;
	}

	public void setAvgTps(double avgTps) {
		this.avgTps = avgTps;
	}

	public int getRunNum() {
		return runNum;
	}

	public void setRunNum(int runNum) {
		this.runNum = runNum;
	}
}
