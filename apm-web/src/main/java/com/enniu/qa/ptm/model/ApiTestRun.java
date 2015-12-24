package com.enniu.qa.ptm.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;
import org.ngrinder.common.util.DateUtils;
import org.ngrinder.model.*;
import org.ngrinder.model.Cloneable;

import javax.persistence.*;

import static org.ngrinder.common.util.AccessUtils.getSafe;

/**
 * Created by fuyong on 7/3/15.
 */
@Entity
@Table(name = "t_api_run")
public class ApiTestRun extends BaseModel<ApiTestRun>{

	private static final int MAX_STRING_SIZE = 2048;
	private static final int MAX_STATE_SIZE = 4096;

	@ManyToOne
	@JoinColumn(name = "api_id",referencedColumnName = "id")
	private Api api;

	@Expose
	@Cloneable
	@Column(length = MAX_STRING_SIZE)
	private String description;

	@OneToOne(mappedBy = "apiTestRun")
	private TestReport report;

	@OneToOne(mappedBy = "apiTestRun")
	private ApiRunTestConfig runConfig;

	@OneToOne()
	@JoinColumn(name = "commit_id",referencedColumnName = "id")
	private Commit commit;

	private Status status;

	@Column(name = "progress_message", length = MAX_STRING_SIZE)
	private String progressMessage;

	@Column(name = "last_progress_message", length = MAX_STRING_SIZE)
	private String lastProgressMessage;

	@Column(name = "running_sample", length = MAX_STATE_SIZE)
	private String runningSample;

	@Column(name = "agent_stat", length = MAX_STATE_SIZE)
	private String agentState;

	@Column(name = "monitor_stat", length = MAX_STATE_SIZE)
	private String monitorState;

	@Column(name = "test_error_cause")
	@Enumerated(EnumType.STRING)
	private Status testErrorCause;

	@Expose
	@Column(name = "stop_request")
	@Type(type = "true_false")
	private Boolean stopRequest;

	/**
	 * Console port for this test. This is the identifier for console
	 */
	@Column(name = "port")
	private Integer port;

	@Column(name = "test_comment", length = MAX_STRING_SIZE)
	private String testComment;

	public String getTestIdentifier() {
		return "perftest_" + getId() + "_" + getLastModifiedUser().getUserId();
	}


	@PrePersist
	@PreUpdate
	public void init(){
		runConfig.init();
		this.description=getSafe(this.description,"");
		this.progressMessage=getSafe(this.progressMessage,"");
		this.lastProgressMessage=getSafe(this.lastProgressMessage,"");
		this.runningSample=getSafe(this.runningSample,"");
		this.stopRequest=getSafe(this.stopRequest,false);
		this.port=getSafe(this.port,0);
		this.testComment=getSafe(this.testComment,"");
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApiRunTestConfig getRunConfig() {
		return runConfig;
	}

	public void setRunConfig(ApiRunTestConfig runConfig) {
		this.runConfig = runConfig;
	}


	public String getTestComment() {
		return testComment;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public TestReport getReport() {
		return report;
	}

	public void setReport(TestReport report) {
		this.report = report;
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

	public void setTestComment(String testComment) {
		this.testComment = testComment;
	}

	public Boolean getStopRequest() {
		return stopRequest;
	}

	public void setStopRequest(Boolean stopRequest) {
		this.stopRequest = stopRequest;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Status getTestErrorCause() {
		return testErrorCause;
	}

	public void setTestErrorCause(Status errorCause) {
		this.testErrorCause = errorCause;
	}

	public String getRunningSample() {
		return runningSample;
	}

	public void setRunningSample(String runningSample) {
		this.runningSample = runningSample;
	}

	public String getAgentState() {
		return agentState;
	}

	public void setAgentState(String agentState) {
		this.agentState = agentState;
	}

	public String getMonitorState() {
		return monitorState;
	}

	public void setMonitorState(String monitorState) {
		this.monitorState = monitorState;
	}

	public String getProgressMessage() {
		return progressMessage;
	}

	public void setProgressMessage(String progressMessage) {
		this.progressMessage = progressMessage;
	}

	public String getLastProgressMessage() {
		return lastProgressMessage;
	}

	public void setLastProgressMessage(String lastProgressMessage) {
		this.lastProgressMessage = lastProgressMessage;
	}


	/**
	 * Clear the last progress message.
	 */
	public void clearLastProgressMessage() {
		this.lastProgressMessage = "";
	}

	/**
	 * Clear all messages.
	 */
	public void clearMessages() {
		clearLastProgressMessage();
		setProgressMessage("");
	}
	public String getLastModifiedDateToStr() {
		return DateUtils.dateToString(getLastModifiedDate());
	}

	/**
	 * Get Running time in HH:MM:SS style.
	 *
	 * @return formatted runtime string
	 */
	public String getRuntimeStr() {
		long ms = (this.getLastModifiedDate() == null || this.getCreatedDate() == null) ? 0 : this.getLastModifiedDate().getTime()
				- this.getCreatedDate().getTime();
		return DateUtils.ms2Time(ms);
	}
}
