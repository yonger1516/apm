package com.enniu.qa.ptm.model;

import org.ngrinder.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Id;

/**
 * Created by fuyong on 7/20/15.
 */
@Entity
@Table(name="t_traffic")
public class Traffic extends BaseModel<Traffic> {

	@Column(name = "project_id")
	private long projectId;

	@Column(name = "api_id")
	private long apiId;
	private double rate;
	private TimeUnitEnum unit;
	private int version;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getApiId() {
		return apiId;
	}

	public void setApiId(long apiId) {
		this.apiId = apiId;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
