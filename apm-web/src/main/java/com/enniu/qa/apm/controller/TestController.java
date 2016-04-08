package com.enniu.qa.apm.controller;

import com.enniu.qa.apm.configuration.Config;
import com.enniu.qa.apm.configuration.constant.ControllerConstants;
import com.enniu.qa.apm.model.*;
import com.enniu.qa.apm.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;
import org.ngrinder.model.*;
import org.ngrinder.model.RampUp;
import org.ngrinder.service.IAgentManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.google.common.collect.Lists.newArrayList;
import static org.ngrinder.common.util.CollectionUtils.buildMap;
import static org.ngrinder.common.util.CollectionUtils.newHashMap;
import static org.ngrinder.common.util.Preconditions.checkArgument;
import static org.ngrinder.common.util.Preconditions.checkNotNull;

/**
 * Created by fuyong on 6/30/15.
 */

@Controller
@RequestMapping("/test")
public class TestController extends BaseController{
	private static final Logger logger= LoggerFactory.getLogger(TestController.class.getSimpleName());

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private PerfTestService perfTestService;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private IAgentManagerService agentManagerService;

	@Autowired
	AgentManager agentManager;

	@Autowired
	RegionService regionService;

	@Autowired
	ApiTestConfigService configService;


	@Autowired
	ProjectService projectService;

	@Autowired
	ApiService apiService;

	@Autowired
	CommitService commitService;

	@Autowired
	TestReportService reportService;

	@RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
	public String getRunDetails(@PathVariable("id")long runId, ModelMap model) {
		//ApiTestRun run= perfTestService.getOne(runId);
		ApiTestRun run=null;

		model.addAttribute("test", run);
		model.addAttribute("user", getCurrentUser());

		Map<String, MutableInt> agentCountMap = agentManagerService.getAvailableAgentCountMap(getCurrentUser());

		model.addAttribute(PARAM_REGION_AGENT_COUNT_MAP, agentCountMap);
		model.addAttribute(PARAM_REGION_LIST, regionService.getAllVisibleRegionNames());
		model.addAttribute(PARAM_PROCESS_THREAD_POLICY_SCRIPT, perfTestService.getProcessAndThreadPolicyScript());
		addDefaultAttributeOnModel(model);
		return "/test/detail";
	}

	@RequestMapping(value = "perfMetrics", method = RequestMethod.GET)
	public String getPerformanceMetrics(@RequestParam("id") int id, ModelMap map) {

		return "/test/perfMetrics";
	}

	@RequestMapping(value = "server", method = RequestMethod.GET)
	public String getServerMetrics(@RequestParam("id") int id, ModelMap map) {
		return "/test/server";
	}

	@RequestMapping(value = "jvm", method = RequestMethod.GET)
	public String getJvmMetrics(@RequestParam("id") int id, ModelMap map) {
		return "/test/jvm";
	}

	@RequestMapping(value = "mix", method = RequestMethod.GET)
	public String getMixMetrics(@RequestParam("id") int id, ModelMap map) {
		return "/test/mix";
	}

	@RequestMapping(value = "/start",method = RequestMethod.GET)
	public String testRunning(long id,RunSourceEnum source,long commitId,ModelMap map){
		ApiTestRun run=new ApiTestRun();

		return "redirect:/test/details?runId="+run.getId();
	}

	@RequestMapping(value = "config",method = RequestMethod.GET)
	public String getConfig(long id,ModelMap model){
		ApiTestConfig config=configService.findById(id);

		model.addAttribute("test",config);
		model.addAttribute("user", getCurrentUser());

		Map<String, MutableInt> agentCountMap = agentManagerService.getAvailableAgentCountMap(getCurrentUser());

		model.addAttribute(PARAM_REGION_AGENT_COUNT_MAP, agentCountMap);
		model.addAttribute(PARAM_REGION_LIST, regionService.getAllVisibleRegionNames());
		model.addAttribute(PARAM_PROCESS_THREAD_POLICY_SCRIPT, perfTestService.getProcessAndThreadPolicyScript());
		addDefaultAttributeOnModel(model);
		return "/test/config";
	}

	/*@RequestMapping(value = "/config/new",method = RequestMethod.POST)
	@ResponseBody
	public Object saveConfig(ApiTestConfig config){
		*//*validate(getCurrentUser(), config);
		config.setScriptRevision(-1L);
		configService.save(getCurrentUser(), config);

		Map map=new HashMap();
		map.put("success", true);*//*
		return map;
	}*/

	@RequestMapping(value = "script",method = RequestMethod.GET)
	public String testScript(){

		return "/test/script";
	}

	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteRun(Integer runId) throws Exception{
		logger.info("delete run:" + runId);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("success", "true");
		return map;
	}


	/*@RestAPI
	@RequestMapping("/api/{id}/status")
	public HttpEntity<String> getStatus(@PathVariable("id") Long id) {
		List<ApiTestRun> perfTests = perfTestService.getAll(getCurrentUser(), new Long[]{id});
		return toJsonHttpEntity(buildMap("status", getStatus(perfTests)));
	}
*/

	/*@RequestMapping(value = "/{id}/running_div")
	public String getRunningSection(ModelMap model, @PathVariable long id) {
		User user=getCurrentUser();
		ApiTestRun test = getOneWithPermissionCheck(user, id);
		model.addAttribute(PARAM_TEST, test);
		return "/test/running";
	}*/


	/*@RequestMapping(value = "/{id}/basic_report")
	public String getReportSection(ModelMap model, @PathVariable long id, @RequestParam int imgWidth) {
		User user=getCurrentUser();
		ApiTestRun test = getOneWithPermissionCheck(user, id);
		int interval = perfTestService2.getReportDataInterval(id, "TPS", imgWidth);
		model.addAttribute(PARAM_LOG_LIST, perfTestService2.getLogFiles(id));
		model.addAttribute(PARAM_TEST_CHART_INTERVAL, interval * test.getRunConfig().getSamplingInterval());
		model.addAttribute(PARAM_TEST, test);

		model.addAttribute(PARAM_TPS, perfTestService2.getSingleReportDataAsJson(id, "TPS", interval));
		return "/test/basic_report";
	}

	@RequestMapping(value = "/{id}/api/sample")
	@RestAPI
	public HttpEntity<String> refreshTestRunning(@PathVariable("id") long id) {
		User user=getCurrentUser();
		ApiTestRun test = checkNotNull(getOneWithPermissionCheck(user, id), "given test should be exist : " + id);
		Map<String, Object> map = newHashMap();
		//map.put("status", test.getStatus());
		map.put("perf", perfTestService2.getStatistics(test));
		map.put("agent", perfTestService2.getAgentStat(test));
		map.put("monitor", perfTestService2.getMonitorStat(test));
		return toJsonHttpEntity(map);
	}

	private List<Map<String, Object>> getStatus(List<ApiTestRun> perfTests) {
		List<Map<String, Object>> statuses = newArrayList();
		for (ApiTestRun each : perfTests) {
			Map<String, Object> result = newHashMap();
			result.put("id", each.getId());
			result.put("status_id", each.getStatus());
			result.put("status_type", each.getStatus());
			result.put("name", getMessages(each.getStatus().getSpringMessageKey()));
			result.put("icon", each.getStatus().getIconName());
			result.put("message",
					StringUtils.replace(each.getProgressMessage() + "\n<b>" + each.getLastProgressMessage() + "</b>\n"
							+ each.getLastModifiedDateToStr(), "\n", "<br/>"));
			result.put("deletable", each.getStatus().isDeletable());
			result.put("stoppable", each.getStatus().isStoppable());
			result.put("reportable", each.getStatus().isReportable());
			statuses.add(result);
		}
		return statuses;
	}


	private ApiTestRun getOneWithPermissionCheck(User user, Long id) {
		ApiTestRun run = perfTestService2.getOne(id);
		if (user.getRole().equals(org.ngrinder.model.Role.ADMIN) || user.getRole().equals(org.ngrinder.model.Role.SUPER_USER)) {
			return run;
		}
		if (run != null && !user.equals(run.getCreatedUser())) {
			run=null;
		}
		return run;
	}*/
	/**
	 * Add the various default configuration values on the model.
	 *
	 * @param model model to which put the default values
	 */
	public void addDefaultAttributeOnModel(ModelMap model) {
		model.addAttribute(PARAM_AVAILABLE_RAMP_UP_TYPE, RampUp.values());
		model.addAttribute(PARAM_MAX_VUSER_PER_AGENT, agentManager.getMaxVuserPerAgent());
		model.addAttribute(PARAM_MAX_RUN_COUNT, agentManager.getMaxRunCount());
		model.addAttribute(PARAM_SECURITY_MODE, getConfig().isSecurityEnabled());
		model.addAttribute(PARAM_MAX_RUN_HOUR, agentManager.getMaxRunHour());
		model.addAttribute(PARAM_SAFE_FILE_DISTRIBUTION,
				getConfig().getControllerProperties().getPropertyBoolean(ControllerConstants.PROP_CONTROLLER_SAFE_DIST));
		String timeZone = getCurrentUser().getTimeZone();
		int offset;
		if (StringUtils.isNotBlank(timeZone)) {
			offset = TimeZone.getTimeZone(timeZone).getOffset(System.currentTimeMillis());
		} else {
			offset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
		}
		model.addAttribute(PARAM_TIMEZONE_OFFSET, offset);
	}

	private void validate(User user,ApiTestConfig config) {
		
		if (config.isThresholdRunCount()) {
			final Integer runCount = config.getRunCount();
			checkArgument(runCount > 0 && runCount <= agentManager
							.getMaxRunCount(),
					"runCount should be equal to or less than %s", agentManager.getMaxRunCount());
		} else {
			final Long duration = config.getDuration();
			checkArgument(duration > 0 && duration <= (((long) agentManager.getMaxRunHour()) *
							3600000L),
					"duration should be equal to or less than %s", agentManager.getMaxRunHour());
		}
		Map<String, MutableInt> agentCountMap = agentManagerService.getAvailableAgentCountMap(user);
		MutableInt agentCountObj = agentCountMap.get(isClustered() ? config.getRegion() : Config.NONE_REGION);
		checkNotNull(agentCountObj, "region should be within current region list");
		int agentMaxCount = agentCountObj.intValue();
		checkArgument(config.getAgentCount() <= agentMaxCount, "test agent should be equal to or less than %s",
				agentMaxCount);


		checkArgument(config.getVuserPerAgent() <= agentManager.getMaxVuserPerAgent(),
				"vuserPerAgent should be equal to or less than %s", agentManager.getMaxVuserPerAgent());
		if (getConfig().isSecurityEnabled()) {
			checkArgument(StringUtils.isNotEmpty(config.getTargetHosts()),
					"targetHosts should be provided when security mode is enabled");
		}

		checkArgument(config.getVuserPerAgent() == config.getProcesses() * config.getThreads(),
				"vuserPerAgent should be equal to (processes * threads)");
	}

}
