package com.enniu.qa.apm.controller;

import com.enniu.qa.apm.configuration.constant.ControllerConstants;
import com.enniu.qa.apm.dto.*;
import com.enniu.qa.apm.handler.ApiHandler;
import com.enniu.qa.apm.model.*;

import com.enniu.qa.apm.service.*;
import org.apache.commons.lang.StringUtils;
import org.ngrinder.model.RampUp;
import org.ngrinder.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import static com.enniu.qa.apm.util.CommonUtils.*;

/**
 * Created by fuyong on 7/6/15.
 */

@Controller
@RequestMapping("/project/{projectId}/api")
public class APIController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(APIController.class.getSimpleName());

	@Autowired
	ApiHandler handler;

	@Autowired
	AgentManager agentManager;

	@Autowired
	ProjectService projectService;


	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String getApiList(@PathVariable("projectId")long projectId,ModelMap map){
		List<ApiStatDto> apiBasicDTOs=handler.getApiBasicInfos(projectId);

		map.put("apis", apiBasicDTOs);
		map.put("projectId",projectId);

		return "/project/apis";

	}

	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public String getAPIDetails(@PathVariable("id")long apiId, ModelMap map) {

		Api api=handler.getApiById(apiId);
		map.put("api", api);
		return "/api/details";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newAPI(@PathVariable("projectId")long projectId, ModelMap map) {

		Api api = new Api();
		map.put("api", api);
		map.put("project",projectService.getProjectById(projectId));
		return "/api/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.PUT)
	@ResponseBody
	public Object addAPI(@PathVariable("projectId")long projectId,ApiBasicDto apiBasicDto) throws Exception{

		Api api=handler.create(currentUser(), projectId, apiBasicDto);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",api.getId());
		map.put("success",true);
		return map;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Object remove(@PathVariable("projectId")long projectId,@PathVariable("id")long apiId) throws Exception {

		handler.delete(apiId);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	/*
	* api info edit
	* */
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("projectId") long projectId, @PathVariable("id")long id,ModelMap map) {
		Api api=handler.getApiById(id);
		map.put("api",api);
		map.put("project", projectService.getProjectById(projectId));
		return "/api/edit";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String doEdit(@PathVariable("projectId")long projectId,@PathVariable("id")long apiId,ApiBasicDto dto, ModelMap map) {

		handler.update(apiId, dto);
		return "redirect:/project/"+projectId+"/api/"+apiId;
	}



	@RequestMapping(value = "/{apiId}/overall", method = RequestMethod.GET)
	public String getAPIOverall(@PathVariable("projectId")long projectId,@PathVariable("apiId") int apiId, ModelMap map) {


		List<TestResultDTO> resultDTOs=handler.getTestResults(apiId);

		/*ApiStatDto apiDTO=new ApiStatDto();
		apiDTO.setId(id);*/

		Api api=handler.getApiById(apiId);
		map.put("api", api);
		map.put("results", resultDTOs);
		//map.put("stat", apiDTO);

		return "/api/overall";
	}


	/*
	* api config
	* */
	@RequestMapping(value = "/{id}/config",method = RequestMethod.GET)
	public String getConfig(@PathVariable("id")long apiId,ModelMap model){

		model.addAttribute("test",handler.getConfig(apiId));
		model.addAttribute("user", getCurrentUser());


		model.addAttribute(PARAM_REGION_AGENT_COUNT_MAP, handler.getAgentInfo(getCurrentUser()));
		model.addAttribute(PARAM_REGION_LIST, handler.getRegionList());
		//model.addAttribute(PARAM_PROCESS_THREAD_POLICY_SCRIPT, handler.getPerfTestPolicyScript());
		addDefaultAttributeOnModel(model);
		return "/api/config";
	}

	@RequestMapping(value = "{id}/config",method = RequestMethod.PUT)
	@ResponseBody
	public Object newConfig(@PathVariable("id")long apiId,ApiTestConfig config){
		ConfigResult res=new ConfigResult();
		try {
			ApiTestConfig apiTestConfig = handler.saveConfig(currentUser(), apiId, config);

			res.setSuccess(true);
			res.setMessage("");
		}catch (Exception e){
			res.setSuccess(false);
			res.setMessage(e.getMessage());
		}

		Map map=new HashMap();
		map.put("success", res.isSuccess());
		map.put("msg", res.getMessage());
		return map;
	}

	@RequestMapping(value = "{apiId}/config",method = RequestMethod.POST)
	public Object updateConfig(@PathVariable("projectId")long projectId,@PathVariable("apiId")long apiId,ApiTestConfig config){

		ApiTestConfig apiTestConfig = handler.saveConfig(currentUser(), apiId, config);
		return "redirect:/project/"+projectId+"/api/"+apiId;

	}

	@RequestMapping(value = "/{apiId}/start")
	public String startTest(@PathVariable("apiId")long apiId,RedirectAttributes attr){

		ApiTestConfig config=handler.getConfig(apiId);
		String testName=config.getApi().getName()+"_"+getDateStr();
		attr.addAttribute("testName",testName);
		attr.addAttribute("tagString","");
		attr.addAttribute("description","");
		attr.addAttribute("agentCount",config.getAgentCount());
		attr.addAttribute("vuserPerAgent",config.getVuserPerAgent());
		attr.addAttribute("processes",config.getProcesses());
		attr.addAttribute("threads",config.getThreads());
		attr.addAttribute("scriptName",config.getScriptName());
		attr.addAttribute("scriptRevision",config.getScriptRevision());
		attr.addAttribute("targetHosts",config.getTargetHosts());
		attr.addAttribute("threshold",config.getThreshold());
		attr.addAttribute("duration",config.getDuration());
		//attr.addAttribute("durationHour",config.getDurationStr());
		attr.addAttribute("runCount",config.getRunCount());
		attr.addAttribute("samplingInterval",config.getSamplingInterval());
		attr.addAttribute("ignoreSampleCount",config.getIgnoreSampleCount());

		attr.addAttribute("param",config.getParam());
		attr.addAttribute("rampUpType",config.getRampUpType());
		attr.addAttribute("rampUpInitCount",config.getRampUpInitCount());
		attr.addAttribute("rampUpStep",config.getRampUpStep());
		attr.addAttribute("rampUpInitSleepTime",config.getRampUpInitSleepTime());
		attr.addAttribute("rampUpIncrementInterval",config.getRampUpIncrementInterval());

		attr.addAttribute("status", Status.READY);

		ApiTestRun run=handler.start(apiId, testName);
		attr.addAttribute("runId",run.getId());

		return "redirect:/perftest/new";
	}


	@RequestMapping(value = "/{apiId}/{runId}",method = RequestMethod.GET)
	public String perfTest(@PathVariable("apiId")long apiId,@PathVariable("runId")long runId){
		ApiTestRun run=handler.getOneRun(apiId,runId);
		return "redirect:/perftest/"+run.getPerfTestId();
	}

	@RequestMapping(value = "/{apiId}/{runId}",method = RequestMethod.DELETE)
	@ResponseBody
	public Object deletePerfTest(@PathVariable("apiId")long apiId,@PathVariable("runId")long runId){
		handler.deleteTestRun(apiId, runId);

		Map map=new HashMap();
		map.put("success", true);
		map.put("msg", "");
		return map;
	}


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

}
