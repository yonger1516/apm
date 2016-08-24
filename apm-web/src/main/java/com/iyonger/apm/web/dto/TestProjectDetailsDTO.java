package com.iyonger.apm.web.dto;

/**
 * Created by fuyong on 7/15/15.
 */
public class TestProjectDetailsDTO {
	private String projectName;
	private long projectId;
	private long runId;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
