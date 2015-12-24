package com.enniu.qa.ptm.dto;

import org.ngrinder.model.Status;

import java.util.Date;

/**
 * Created by fuyong on 7/15/15.
 */
public class TestResultDTO {

	private long runId;
	private long commitId;
	private Date startTime;
	private long duration;
	private Status status;
	private double hps;
	private double apdex;
	private double avgRT;
	private double tps;
	private double error;
	private double success;

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

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getHps() {
		return hps;
	}

	public void setHps(double hps) {
		this.hps = hps;
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

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getSuccess() {
		return success;
	}

	public void setSuccess(double success) {
		this.success = success;
	}
}
