package com.iyonger.apm.web.model;

import org.ngrinder.model.*;

import javax.persistence.*;

import static org.ngrinder.common.util.AccessUtils.getSafe;

/**
 * Created by fuyong on 7/3/15.
 */
@Entity
@Table(name = "t_api_run")
public class ApiTestRun extends BaseModel<ApiTestRun>{


	@Column(name = "perftest_id")
	private long perfTestId;

	@Column(name = "perftest_name")
	private String perfTestName;


	@ManyToOne
	@JoinColumn(name = "api_id",referencedColumnName = "id")
	private Api api;

	public long getPerfTestId() {
		return perfTestId;
	}

	public void setPerfTestId(long perfTestId) {
		this.perfTestId = perfTestId;
	}

	public String getPerfTestName() {
		return perfTestName;
	}

	public void setPerfTestName(String perfTestName) {
		this.perfTestName = perfTestName;
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}
}
