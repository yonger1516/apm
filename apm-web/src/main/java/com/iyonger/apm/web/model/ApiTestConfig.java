package com.iyonger.apm.web.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.*;
import org.ngrinder.common.util.DateUtils;
import org.ngrinder.model.*;
import org.ngrinder.model.Cloneable;
import org.ngrinder.model.RampUp;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

import static org.ngrinder.common.util.AccessUtils.getSafe;

/**
 * Created by fuyong on 8/19/15.
 */
@Entity
@Table(name = "t_api_config")
public class ApiTestConfig extends BaseModel<ApiTestConfig> {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "api_id",referencedColumnName = "id")
	private Api api;

	/**
	 * Use rampUp or not.
	 */
	@Expose
	@org.ngrinder.model.Cloneable
	@Column(name = "use_rampup", columnDefinition = "char(1)")
	@Type(type = "true_false")
	private Boolean useRampUp;

	/**
	 * Use rampUp or not.
	 */
	@Expose
	@Cloneable
	@Column(name = "ramp_up_type")
	@Enumerated(EnumType.STRING)
	private RampUp rampUpType;


	/**
	 * The threshold code, R for run count; D for duration.
	 */
	@Expose
	@Cloneable
	@Column(name = "threshold")
	private String threshold;

	@Expose
	@Cloneable
	@Column(name = "duration")
	private Long duration;

	@Expose
	@Cloneable
	@Column(name = "run_count")
	private Integer runCount;

	@Expose
	@Cloneable
	@Column(name = "agent_count")
	private Integer agentCount;

	@Expose
	@Cloneable
	@Column(name = "vuser_per_agent")
	private Integer vuserPerAgent;

	@Expose
	@Cloneable
	@Column(name = "processes")
	private Integer processes;

	@Expose
	@Cloneable
	@Column(name = "ramp_up_init_count")
	private Integer rampUpInitCount;

	@Expose
	@Cloneable
	@Column(name = "ramp_up_init_sleep_time")
	private Integer rampUpInitSleepTime;

	@Expose
	@Cloneable
	@Column(name = "ramp_up_step")
	private Integer rampUpStep;

	@Expose
	@Cloneable
	@Column(name = "ramp_up_increment_interval")
	private Integer rampUpIncrementInterval;

	@Expose
	@Cloneable
	@Column(name = "threads")
	private Integer threads;

	@Expose
	@Cloneable
	@Column(name = "region")
	private String region;

	@Expose
	@Cloneable
	@Column(name = "script_name")
	private String scriptName;

	@Expose
	@Column(name = "script_revision")
	private Long scriptRevision;

	/**
	 * the target host to test.
	 */
	@Expose
	@Cloneable
	@Column(name = "target_hosts")
	private String targetHosts;

	/**
	 * The send mail code.
	 */
	@Expose
	@Cloneable
	@Column(name = "send_mail", columnDefinition = "char(1)")
	@Type(type = "true_false")
	private Boolean sendMail;

	@Column(name = "safe_distribution")
	@Cloneable
	@Type(type = "true_false")
	private Boolean safeDistribution;

	@Expose
	@Cloneable
	@Column(name = "sampling_interval")
	private Integer samplingInterval;

	@Expose
	@Cloneable
	@Column(name = "param")
	private String param;

	@Expose
	@Cloneable
	/** ignoreSampleCount value, default to 0. */
	@Column(name = "ignore_sample_count")
	private Integer ignoreSampleCount;

	@Expose
	/** the scheduled time of this test. */
	@Column(name = "scheduled_time")
	@org.hibernate.annotations.Index(name = "scheduled_time_index")
	private Date scheduledTime;

	@Expose
	@Cloneable
	@Column(name = "agent_id")
	private String agentId;


	@Column(name = "use_fixedrate_rps", columnDefinition = "char(1)")
	@Type(type = "true_false")
	private Boolean useFixedRateRPS;

	@Column(name = "rps")
	private Double rps;

	@PrePersist
	@PreUpdate
	public void init() {

		this.agentCount = getSafe(this.agentCount);

		this.processes = getSafe(this.processes, 1);
		this.threads = getSafe(this.threads, 1);
		this.scriptName = getSafe(this.scriptName, "");

		this.threshold = getSafe(this.threshold, "D");
		if (isThresholdRunCount()) {
			this.setIgnoreSampleCount(0);
		} else {
			this.ignoreSampleCount = getSafe(this.ignoreSampleCount);
		}
		this.runCount = getSafe(this.runCount);
		this.duration = getSafe(this.duration, 60000L);
		this.samplingInterval = getSafe(this.samplingInterval, 2);
		this.scriptRevision = getSafe(this.scriptRevision, -1L);
		this.param = getSafe(this.param, "");
		this.region = getSafe(this.region, "NONE");
		this.targetHosts = getSafe(this.targetHosts, "");

		this.vuserPerAgent = getSafe(this.vuserPerAgent, 1);
		this.safeDistribution = getSafe(this.safeDistribution, false);
		this.useRampUp = getSafe(this.useRampUp, false);
		this.rampUpInitCount = getSafe(this.rampUpInitCount, 0);
		this.rampUpStep = getSafe(this.rampUpStep, 1);
		this.rampUpInitSleepTime = getSafe(this.rampUpInitSleepTime, 0);
		this.rampUpIncrementInterval = getSafe(this.rampUpIncrementInterval, 1000);
		this.rampUpType = getSafe(this.rampUpType, RampUp.PROCESS);
		this.useFixedRateRPS=getSafe(this.useFixedRateRPS,false);
		this.rps=getSafe(this.rps,0.0);
		this.agentId=getSafe(this.agentId,"");
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Double getRps() {
		return rps;
	}

	public void setRps(Double rps) {
		this.rps = rps;
	}

	public void setUseFixedRateRPS(Boolean useFixedRateRPS) {
		this.useFixedRateRPS = useFixedRateRPS;
	}

	public boolean getUseFixedRateRPS() {
		return useFixedRateRPS;
	}

	public org.ngrinder.model.RampUp getRampUpType() {
		return rampUpType;
	}

	public void setRampUpType(org.ngrinder.model.RampUp rampUpType) {
		this.rampUpType = rampUpType;
	}

	public Boolean isThresholdDuration() {
		return "D".equals(getThreshold());
	}

	public long getTotalRunCount() {
		return getAgentCount() * getThreads() * getProcesses() * (long) getRunCount();
	}

	public Boolean isThresholdRunCount() {
		return "R".equals(getThreshold());
	}

	public Boolean getSafeDistribution() {
		return safeDistribution;
	}

	public void setSafeDistribution(Boolean safeDistribution) {
		this.safeDistribution = safeDistribution;
	}

	public Integer getSamplingInterval() {
		return samplingInterval;
	}

	public void setSamplingInterval(Integer samplingInterval) {
		this.samplingInterval = samplingInterval;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getIgnoreSampleCount() {
		return ignoreSampleCount;
	}

	public void setIgnoreSampleCount(Integer ignoreSampleCount) {
		this.ignoreSampleCount = ignoreSampleCount;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getTargetHosts() {
		return targetHosts;
	}

	public void setTargetHosts(String targetHosts) {
		this.targetHosts = targetHosts;
	}

	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Long getScriptRevision() {
		return scriptRevision;
	}

	public void setScriptRevision(Long scriptRevision) {
		this.scriptRevision = scriptRevision;
	}

	public Boolean getUseRampUp() {
		return useRampUp;
	}

	public void setUseRampUp(Boolean useRampUp) {
		this.useRampUp = useRampUp;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getRunCount() {
		return runCount;
	}

	public void setRunCount(Integer runCount) {
		this.runCount = runCount;
	}

	public Integer getAgentCount() {
		return agentCount;
	}

	public void setAgentCount(Integer agentCount) {
		this.agentCount = agentCount;
	}

	public Integer getVuserPerAgent() {
		return vuserPerAgent;
	}

	public void setVuserPerAgent(Integer vuserPerAgent) {
		this.vuserPerAgent = vuserPerAgent;
	}

	public Integer getProcesses() {
		return processes;
	}

	public void setProcesses(Integer processes) {
		this.processes = processes;
	}

	public Integer getRampUpInitCount() {
		return rampUpInitCount;
	}

	public void setRampUpInitCount(Integer rampUpInitCount) {
		this.rampUpInitCount = rampUpInitCount;
	}

	public Integer getRampUpInitSleepTime() {
		return rampUpInitSleepTime;
	}

	public void setRampUpInitSleepTime(Integer rampUpInitSleepTime) {
		this.rampUpInitSleepTime = rampUpInitSleepTime;
	}

	public Integer getRampUpStep() {
		return rampUpStep;
	}

	public void setRampUpStep(Integer rampUpStep) {
		this.rampUpStep = rampUpStep;
	}

	public Integer getRampUpIncrementInterval() {
		return rampUpIncrementInterval;
	}

	public void setRampUpIncrementInterval(Integer rampUpIncrementInterval) {
		this.rampUpIncrementInterval = rampUpIncrementInterval;
	}

	public Integer getThreads() {
		return threads;
	}

	public void setThreads(Integer threads) {
		this.threads = threads;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDurationStr() {
		return DateUtils.ms2Time(this.duration);
	}



}
