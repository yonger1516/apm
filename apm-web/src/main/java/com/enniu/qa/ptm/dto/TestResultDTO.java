package com.enniu.qa.ptm.dto;

import org.ngrinder.model.Status;

import java.util.Date;

/**
 * Created by fuyong on 7/15/15.
 */
public class TestResultDTO {

	private int id;
	private long runId;
	private long commitId;
	private Date startTime;
	private String duration;
	private Status status;
	private long tests;
	private double rps;
	private double tps;
	private double apdex;
	private double avgRT;
	private long errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public long getCommitId() {
		return commitId;
	}

	public void setCommitId(long commitId) {
		this.commitId = commitId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public double getApdex() {
		return apdex;
	}

	public void setApdex(double apdex) {
		this.apdex = apdex;
	}

	public double getAvgRT() {
		return avgRT;
	}

	public void setAvgRT(double avgRT) {
		this.avgRT = avgRT;
	}

	public double getTps() {
		return tps;
	}

	public void setTps(double tps) {
		this.tps = tps;
	}

	public long getTests() {
		return tests;
	}

	public void setTests(long tests) {
		this.tests = tests;
	}

	public double getRps() {
		return rps;
	}

	public void setRps(double rps) {
		this.rps = rps;
	}

	public long getErrors() {
		return errors;
	}

	public void setErrors(long errors) {
		this.errors = errors;
	}

}




