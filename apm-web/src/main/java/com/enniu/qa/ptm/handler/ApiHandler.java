package com.enniu.qa.ptm.handler;

import com.enniu.qa.ptm.configuration.Config;
import com.enniu.qa.ptm.dto.ApiStatDto;
import com.enniu.qa.ptm.dto.ApiBasicDto;
import com.enniu.qa.ptm.dto.TestResultDTO;
import com.enniu.qa.ptm.model.*;
import com.enniu.qa.ptm.service.*;
import com.enniu.qa.ptm.util.ReportAggregate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;
import org.ngrinder.model.Status;
import org.ngrinder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.ngrinder.common.util.Preconditions.checkArgument;
import static org.ngrinder.common.util.Preconditions.checkNotEmpty;
import static org.ngrinder.common.util.Preconditions.checkNotNull;

/**
 * Created by fuyong on 7/19/15.
 */

@Service
public class ApiHandler {
	private static final Logger logger = LoggerFactory.getLogger(ApiHandler.class.getSimpleName());


	@Autowired
	ApiService apiService;

	@Autowired
	TestRunService testRunService;

	@Autowired
	TestReportService reportService;

	@Autowired
	TrafficService trafficService;

	@Autowired
	ProjectService projectService;

	@Autowired
	AgentManager agentManager;

	@Autowired
	AgentManagerService agentManagerService;

	@Autowired
	Config config;

	@Autowired
	ApiTestConfigService apiTestConfigService;

	@Autowired
	RunConfigService runConfigService;

	@Autowired
	UserContext context;

	@Autowired
	FileEntryService fileEntryService;

	@Autowired
	RegionService regionService;

	@Autowired
	PerfTestService2 perfTestService2;

	public List<ApiStatDto> getApiBasicInfos(long projectId){
		List<Api> apis=getApisByProject(projectId);

		List<ApiStatDto> apiBasicDTOs = new ArrayList<ApiStatDto>();
		for (Api api : apis) {
			ApiStatDto dto = new ApiStatDto();

			dto.setId(api.getId());
			dto.setName(api.getName());

			Set<ApiTestRun> runs = api.getRuns();
			dto.setRunNum(runs.size());

			List<Long> reportIds = new ArrayList<Long>();
			for (ApiTestRun run : runs) {
				reportIds.add(run.getReport().getId());
			}

			if (0 < reportIds.size()) {
				List<TestReport> reportList = reportService.findByIds(reportIds);
				dto.setAvgApdex(ReportAggregate.getAvgApdex(reportList));
				dto.setAvgRT(ReportAggregate.getAvgRT(reportList));
				dto.setAvgTPS(ReportAggregate.getAvgTps(reportList));
				dto.setSdtDev(ReportAggregate.getAvgSdtDev(reportList));
			}
			apiBasicDTOs.add(dto);
		}

		return apiBasicDTOs;

	}


	public Api update(long apiId,ApiBasicDto dto){
		Api api=new Api();
		api.setId(apiId);

		api.setName(dto.getName());
		api.setStatus(dto.getStatus());
		api.setDescription(dto.getDescription());
		api.setPath(dto.getPath());
		return apiService.save(api);
	}

	public List<Api> getApisByProject(long projectId){
		List<Api> apis=new ArrayList<Api>();
		apis=apiService.findByProjectId(projectId);

		return apis;
	}

	public Api create(User user,long projectId, ApiBasicDto dto) {
		Api api = new Api();
		api.setProject(projectService.getProjectById(projectId));
		api.setName(dto.getName());
		api.setDescription(dto.getDescription());
		api.setPath(dto.getPath());
		api.setStatus(dto.getStatus());
		api.setCreatedDate(new Date());
		api.setCreatedUser(user);
		api.setLastModifiedDate(new Date());
		api.setLastModifiedUser(user);

		api= apiService.save(api);


		ApiTestConfig config = addNewConfig(user, api);
		api.setApiTestConfig(config);

		Traffic traffic = new Traffic();
		traffic.setApiId(api.getId());
		traffic.setUnit(TimeUnitEnum.Second);
		traffic.setRate(0);
		traffic.setVersion(1);
		trafficService.addNewTraffic(traffic);

		return api;
	}

	public Api getApiById(long id){
		return apiService.findById(id);
	}


	public void delete(long apiId){
		apiTestConfigService.deleteByApi(apiService.findById(apiId));
		apiService.removeApi(apiId);
	}


	public ApiTestConfig getConfig(long apiId){
		Api api=apiService.findById(apiId);
		return apiTestConfigService.findByApi(api);
		//return api.getApiTestConfig();
	}

	public List<TestResultDTO> getTestResults(int id) {
		List<TestResultDTO> resultDTOs = new ArrayList<TestResultDTO>();

		Set<ApiTestRun> runList = apiService.findById(id).getRuns();
		for (ApiTestRun run : runList) {
			TestResultDTO dto = new TestResultDTO();
			dto.setRunId(run.getId());
			dto.setStartTime(run.getCreatedDate());
			dto.setCommitId(run.getCommit().getId());
			dto.setStatus(run.getStatus());

			TestReport report = run.getReport();
			dto.setApdex(report.getApdex());
			dto.setAvgRT(report.getAvgRT());
			dto.setDuration(report.getDuration());
			dto.setError(report.getErrorRate());
			dto.setHps(report.getAvgHps());
			dto.setSuccess(report.getSuccessRate());
			dto.setTps(report.getAvgTps());

			resultDTOs.add(dto);

		}


		return resultDTOs;
	}

	public ApiTestConfig addNewConfig(User user, Api api) {
		ApiTestConfig config = new ApiTestConfig();
		config.init();
		config.setApi(api);

		config.setScriptName("");
		config.setScriptRevision(-1L);

		return saveConfig(user,api.getId(),config);
	}

	public ApiTestConfig saveConfig(User user,long apiId,ApiTestConfig config){
		Api api=apiService.findById(apiId);
		config.setApi(api);
		config.setId(api.getApiTestConfig().getId());//fixed config has been init a id after request arriving controller

		ApiTestConfig old= apiTestConfigService.findByApi(api);
		validate(user, old, config);

		if (null!=config.getScriptName()&&!config.getScriptName().isEmpty()){
			long revision=attachFileRevision(user,config.getScriptName());
			config.setScriptRevision(revision);
		}

		return apiTestConfigService.save(config);

	}

	public Map<String,MutableInt> getAgentInfo(User user){
		return agentManagerService.getAvailableAgentCountMap(user);
	}

	public List<String> getRegionList(){
		return regionService.getAllVisibleRegionNames();
	}

	public String getPerfTestPolicyScript(){
		return perfTestService2.getProcessAndThreadPolicyScript();
	}

	private long attachFileRevision(User user,String scriptName){
		FileEntry scriptEntry=fileEntryService.getOne(user,scriptName);
		return scriptEntry != null ? scriptEntry.getRevision() : -1;
	}


	@SuppressWarnings("ConstantConditions")
	private void validate(User user, ApiTestConfig oldOne, ApiTestConfig newOne) {
		if (oldOne == null) {
			oldOne = new ApiTestConfig();
			oldOne.init();
		}
		newOne = oldOne.merge(newOne);

		if (newOne.isThresholdRunCount()) {
			final Integer runCount = newOne.getRunCount();
			checkArgument(runCount > 0 && runCount <= agentManager
							.getMaxRunCount(),
					"runCount should be equal to or less than %s", agentManager.getMaxRunCount());
		} else {
			final Long duration = newOne.getDuration();
			checkArgument(duration > 0 && duration <= (((long) agentManager.getMaxRunHour()) *
							3600000L),
					"duration should be equal to or less than %s", agentManager.getMaxRunHour());
		}
		Map<String, MutableInt> agentCountMap = agentManagerService.getAvailableAgentCountMap(user);
		MutableInt agentCountObj = agentCountMap.get((config.isClustered()) ? newOne.getRegion() : Config.NONE_REGION);
		checkNotNull(agentCountObj, "region should be within current region list");
		int agentMaxCount = agentCountObj.intValue();
		checkArgument(newOne.getAgentCount() <= agentMaxCount, "test agent should be equal to or less than %s",
				agentMaxCount);

		checkArgument(newOne.getVuserPerAgent() <= agentManager.getMaxVuserPerAgent(),
				"vuserPerAgent should be equal to or less than %s", agentManager.getMaxVuserPerAgent());
		if (config.isSecurityEnabled()) {
			checkArgument(StringUtils.isNotEmpty(newOne.getTargetHosts()),
					"targetHosts should be provided when security mode is enabled");
		}

		checkArgument(newOne.getVuserPerAgent() == newOne.getProcesses() * newOne.getThreads(),
				"vuserPerAgent should be equal to (processes * threads)");
	}

	public ApiTestRun start(long apiId) {
		ApiTestRun run=new ApiTestRun();

		Api api=apiService.findById(apiId);
		run.setApi(api);
		run.setDescription("");

		run.setCreatedUser(context.getCurrentUser());
		run.setLastModifiedUser(context.getCurrentUser());
		run.setCreatedDate(new Date());
		run.setLastModifiedDate(new Date());

		run.set


		ApiRunTestConfig runConfig=new ApiRunTestConfig();

		runConfig.cloneTestConfig(apiService.findById(id).getApiTestConfig());

		runConfig=runConfigService.save(runConfig);
		run.setRunConfig(runConfig);

		/*Commit commit=commitService.save(new Commit());
		run.setCommit(commit);*/

		TestReport report=reportService.save(new TestReport());
		run.setReport(report);
		run.setStatus(Status.READY);


		run= perfTestService2.save(run);
		return null;
	}
}
