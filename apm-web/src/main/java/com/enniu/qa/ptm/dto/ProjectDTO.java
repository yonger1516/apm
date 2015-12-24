package com.enniu.qa.ptm.dto;

import com.enniu.qa.ptm.model.ProjectType;

import java.util.Date;

/**
 * Created by fuyong on 7/10/15.
 */
public class ProjectDTO {
	private long id;
	private String name;
	private long commitId;
	private Date lastCommitDate;
	private int commits;
	private double avgApdex;
	private double avgTPS;
	private int apiNum;
	private ProjectType type;
	private String desc;
	private String creator;
	private Date createTime;

	private double avgRT;
	private double sdtDev;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc=desc;
	}
	public int getCommits(){
		return commits;
	}

	public void setCommits(int commits){
		this.commits=commits;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCommitId() {
		return commitId;
	}

	public void setCommitId(long commitId) {
		this.commitId = commitId;
	}

	public Date getLastCommitDate() {
		return lastCommitDate;
	}

	public void setLastCommitDate(Date lastCommitDate) {
		this.lastCommitDate = lastCommitDate;
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

	public int getApiNum() {
		return apiNum;
	}

	public void setApiNum(int apiNum) {
		this.apiNum = apiNum;
	}
}
