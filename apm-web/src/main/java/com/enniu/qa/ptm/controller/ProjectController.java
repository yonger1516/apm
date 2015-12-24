package com.enniu.qa.ptm.controller;

import com.enniu.qa.ptm.dto.*;
import com.enniu.qa.ptm.handler.ProjectsHandler;
import com.enniu.qa.ptm.model.*;
import com.enniu.qa.ptm.service.*;
import com.enniu.qa.ptm.util.ReportAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by fuyong on 7/1/15.
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class.getSimpleName());

	@Autowired
	ProjectsHandler projectsHandler;

	@RequestMapping(value = "")
	public String list(Map<String, Object> model) {
		List<ProjectDTO> list = projectsHandler.getProjectList();

		model.put("projects", list);
		return "project/list";
	}

	@RequestMapping(value = "new",method = RequestMethod.GET)
	public String newProject(ModelMap model) {
		Project project = new Project();
		model.put("project", project);
		return "/project/new";
	}

	@RequestMapping(value = "new", method = RequestMethod.PUT)
	@ResponseBody
	public Object addNewProject(ProjectBasicDto dto) throws Exception {

		Project newProject = projectsHandler.create(dto);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",newProject.getId());
		map.put("success",true);
		return map;
	}

	@RequestMapping(value = "/{id}/edit",method = RequestMethod.GET)
	public String getEditPage(@PathVariable("id")long projectId,ModelMap map){
		Project project = projectsHandler.getById(projectId);

		map.put("project", project);
		return "/project/edit";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public Object doEdit(@PathVariable("id")long projectId,ProjectBasicDto dto) throws Exception {

		Project project=projectsHandler.update(projectId,dto);

		return "redirect:/project/"+project.getId()+"/details";
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Object remove(@PathVariable("id")long projectId) throws Exception {
		logger.warn("delete project:" + projectId);

		projectsHandler.delete(projectId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "true");
		return map;
	}



	@RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
	public String details(@PathVariable("id")long projectId, ModelMap map) {
		Project project = projectsHandler.getProjectById(projectId);
		map.put("project", project);
		return "/project/details";
	}

	@RequestMapping(value = "/{id}/overall", method = RequestMethod.GET)
	public String overall(@PathVariable("id")long projectId, ModelMap map) {
		Project project = projectsHandler.getProjectById(projectId);

		List<TestRunBasicInfoDTO> runBasicInfoDTOs = new ArrayList<TestRunBasicInfoDTO>();
		/*for (ApiTestRun run : project.getRuns()) {
			TestRunBasicInfoDTO dto = new TestRunBasicInfoDTO();
			dto.setId(run.getId());
			dto.setStartTime(run.getStartTime());
			dto.setCommitId(run.getCommit().getId());
			dto.setStatus(run.getStatus());

			//get test report info
			TestReport report = run.getReport();
			if (null != report) {
				dto.setDuration(report.getDuration());
				dto.setHps(report.getAvgHps());
				dto.setApDex(report.getApdex());
				dto.setAvgRT(report.getAvgRT());
				dto.setSdtDev(report.getSdtDev());
				dto.setMaxTPS(report.getMaxTps());
			}
			runBasicInfoDTOs.add(dto);
		}*/

		map.put("project", project);
		map.put("runs", runBasicInfoDTOs);
		return "/project/overall";
	}

	@RequestMapping(value = "/{id}/trafficModel", method = RequestMethod.GET)
	public String trafficModel(@RequestParam("id") int id, ModelMap map) {
		List<TrafficModelDto> traffics = projectsHandler.getTrafficModel(id);

		map.put("traffics", traffics);
		return "/project/trafficModel";
	}

	@RequestMapping(value = "/{id}/config",method = RequestMethod.GET)
	public String getConfig(@PathVariable("id")long projectId,ModelMap map){
		//get project test configuration
		return "/project/config";
	}


	@RequestMapping(value = "/{id}/start",method = RequestMethod.GET)
	public String testStart(@PathVariable("id")long id,ModelMap map){
		Commit commit=new Commit();
		commit.setCommitTime(new Date());
		commit.setMessage("bug fix");
		commit.setCommittor("performance tester");
		commit.setType(CommitType.Jenkins);

		//commit=commitService.save(commit);
		return "redirect:/test/start?id="+id+"&source="+RunSourceEnum.PROJECT+"&commitId="+commit.getId();
	}

}
