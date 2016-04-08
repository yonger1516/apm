package com.enniu.qa.apm.handler;

import com.enniu.qa.apm.configuration.Config;
import com.enniu.qa.apm.dto.ApiStatDto;
import com.enniu.qa.apm.dto.ApiBasicDto;
import com.enniu.qa.apm.dto.TestResultDTO;
import com.enniu.qa.apm.model.*;
import com.enniu.qa.apm.service.*;
import com.enniu.qa.apm.util.ReportAggregate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;
import org.ngrinder.model.PerfTest;
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
	ApiTestRunService apiTestRunService;

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
	UserContext context;

	@Autowired
	FileEntryService fileEntryService;

	@Autowired
	RegionService regionService;

	@Autowired
	PerfTestService perfTestService;

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
				//reportIds.add(run.getReport().getId());
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


		ApiTestConfig config = addNewConfig(api);
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

		int i=1;
		for (ApiTestRun run : runList) {
			TestResultDTO dto = new TestResultDTO();
			dto.setId(i++);
			dto.setRunId(run.getId());

			PerfTest perfTest=perfTestService.getOne(run.getPerfTestId());
			dto.setStartTime(perfTest.getStartTime());
			dto.setCommitId(0);
			dto.setStatus(perfTest.getStatus());
			dto.setDuration(perfTest.getRuntimeStr());
			dto.setTests(perfTest.getTests());
			dto.setRps(perfTest.getTps());//TODO
			dto.setAvgRT(perfTest.getMeanTestTime());
			dto.setApdex(0);//TODO
			dto.setTps(perfTest.getTps());
			dto.setErrors(perfTest.getErrors());

			resultDTOs.add(dto);

		}


		return resultDTOs;
	}

	public ApiTestConfig addNewConfig(Api api) {
		ApiTestConfig config = new ApiTestConfig();
		config.init();
		config.setApi(api);

		config.setScriptRevision(-1L);

		return apiTestConfigService.save(config);
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

	/*public String getPerfTestPolicyScript(){
		return perfTestService.getProcessAndThreadPolicyScript();
	}*/

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

	public ApiTestRun start(long apiId,String testName) {
		ApiTestRun run=new ApiTestRun();

		Api api=apiService.findById(apiId);

		run.setApi(api);
		run.setCreatedUser(context.getCurrentUser());
		run.setLastModifiedUser(context.getCurrentUser());
		run.setCreatedDate(new Date());
		run.setLastModifiedDate(new Date());

		run.setPerfTestName(testName);


		return apiTestRunService.save(run);
	}


	public ApiTestRun getOneRun(long apiId, long runId) {
		return apiTestRunService.findById(runId);

	}

	public void deleteTestRun(long apiId, long runId) {
		apiTestRunService.deleteRun(runId);
	}
}
