package com.enniu.qa.ptm.handler;

import com.enniu.qa.ptm.controller.TrafficModelDto;
import com.enniu.qa.ptm.dto.*;
import com.enniu.qa.ptm.model.*;
import com.enniu.qa.ptm.service.*;
import com.enniu.qa.ptm.util.ReportAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by fuyong on 7/10/15.
 */

@Service
public class ProjectsHandler {
	private static final Logger logger = LoggerFactory.getLogger(ProjectsHandler.class.getSimpleName());

	@Autowired
	ProjectService projectService;

	@Autowired
	CommitService commitService;

	@Autowired
	ApiTestRunService apiTestRunService;

	@Autowired
	ApiService apiService;

	@Autowired
	TestReportService testReportService;

	@Autowired
	TrafficService trafficService;

	@Autowired
	ApiTestConfigService configService;

	@Autowired
	private UserContext userContext;

	private static final String CACHE_NAME = "apmCache";
	public static final String PROJECT_ALL_KEY = "\"project_all\"";

	@CacheEvict(value = CACHE_NAME, key = PROJECT_ALL_KEY)
	public Project create(ProjectBasicDto dto) {
		Project project = convertDto2Project(dto);

		project.setCreatedDate(new Date());
		project.setCreatedUser(userContext.getCurrentUser());

		project.setLastModifiedUser(userContext.getCurrentUser());
		project.setLastModifiedDate(new Date());
/*
		ApiTestConfig config=new ApiTestConfig();
		config.init();
		config=configService.addNewConfig(new ApiTestConfig());*/
		//project.setApiTestConfig(config);

		return projectService.save(project);

	}

	@CachePut(value = CACHE_NAME, key = "#dto.getId()")
	@CacheEvict(value = CACHE_NAME, key = PROJECT_ALL_KEY)
	public Project update(long projectId,ProjectBasicDto dto){
		Project project=convertDto2Project(dto);
		project.setId(projectId);
		project.setLastModifiedDate(new Date());
		project.setLastModifiedUser(userContext.getCurrentUser());

		return projectService.save(project);
	}


	@Cacheable(value = CACHE_NAME, key = PROJECT_ALL_KEY)
	public List<ProjectDTO> getProjectList() {
		List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();

		List<Project> projects = projectService.getAllProjects();
		for (Project project : projects) {
			//logger.info("project info:"+project.toString());
			ProjectDTO dto = new ProjectDTO();
			dto.setId(project.getId());
			dto.setName(project.getName());
			dto.setDesc(project.getDescription());

			dto.setApiNum(10);
			dto.setAvgApdex(0.9);
			dto.setAvgTPS(10000);
			dto.setCommitId(9527);
			dto.setLastCommitDate(new Date());
			dto.setType(project.getType());


			projectDTOs.add(dto);
		}
		return projectDTOs;
	}

	@Cacheable(value = CACHE_NAME, key = "#id")
	public Project getById(long id) {
		logger.info("not hit,find project name from database");
		Project project = projectService.getProjectById(id);

		/*ProjectBasicDto dto = new ProjectBasicDto();
		dto.setName(project.getName());
		dto.setDescription(project.getDescription());
		dto.setType(project.getType());
		dto.setStatus(project.getStatus());*/
		return project;
	}

	public Project getProjectById(long id){
		return projectService.getProjectById(id);
	}

	@CachePut(value = CACHE_NAME, key = "#id")
	@CacheEvict(value = CACHE_NAME, key = PROJECT_ALL_KEY)
	public void delete(long id) {
		projectService.removeProject(id);
	}

	@Cacheable(value = CACHE_NAME, key = "#id")
	public ProjectDTO getProjectBasicInfo(long id) {
		Project project = projectService.getProjectById(id);

		ProjectDTO dto = new ProjectDTO();
		dto.setId(project.getId());
		dto.setName(project.getName());
		dto.setDesc(project.getDescription());
		dto.setCreateTime(project.getCreatedDate());
		dto.setCreator(project.getCreatedUser() == null ? "admin" : project.getCreatedUser().getUserName());
		dto.setType(project.getType());

		return dto;

	}

	@Cacheable(value = CACHE_NAME, key = "#id+'overall'")
	public ProjectOverallDTO getProjectOverall(long id) {

		ProjectOverallDTO overallDTO = new ProjectOverallDTO();

		Project project = projectService.getProjectById(id);

		ProjectDTO dto = new ProjectDTO();
		dto.setId(project.getId());
		dto.setName(project.getName());
		dto.setDesc(project.getDescription());
		dto.setCreateTime(project.getCreatedDate());
		dto.setCreator(project.getCreatedUser().getUserName());
		dto.setType(project.getType());

		overallDTO.setDto(dto);

		//commit info
		//List<Commit> commits=commitService.findByProjectId(dto.getId());
		//Set<ApiTestRun> runs = project.getRuns();

		List<TestResultDTO> testResultDTOs = new ArrayList<TestResultDTO>();
		/*for (ApiTestRun run : runs) {
			TestResultDTO resultDTO = new TestResultDTO();
			resultDTO.setRunId(run.getId());
			resultDTO.setStartTime(run.getStartTime());
			resultDTO.setStatus(run.getStatus());
			resultDTO.setCommitId(run.getCommit().getId());

			testResultDTOs.add(resultDTO);
		}*/

		overallDTO.setTestResultDTOList(testResultDTOs);

		return overallDTO;
	}


	public List<APIListDTO> getAPIBasic(long projectId) {
		List<APIListDTO> dtoList = new ArrayList<APIListDTO>();

		List<Api> apis = apiService.findByProjectId(projectId);
		for (Api api : apis) {
			APIListDTO dto = new APIListDTO();
			dto.setId(api.getId());
			dto.setName(api.getName());
			dto.setDescription(api.getDescription());

			Set<ApiTestRun> runs = api.getRuns();
			dto.setRunNum(runs.size());

			List<Long> reportIds = new ArrayList<Long>();
			for (ApiTestRun run : runs) {
				//reportIds.add(run.getReport().getId());
			}

			if (0<reportIds.size()){
				List<TestReport> reportList = testReportService.findByIds(reportIds);
				dto.setAvgApdex(ReportAggregate.getAvgApdex(reportList));
				dto.setAvgRT(ReportAggregate.getAvgRT(reportList));
				dto.setAvgTps(ReportAggregate.getAvgTps(reportList));
				dto.setSdtDev(ReportAggregate.getAvgSdtDev(reportList));
			}

			dtoList.add(dto);

		}

		return dtoList;
	}

	public List<TrafficModelDto> getTrafficModel(int id) {
		List<Api> apis = apiService.findByProjectId(id);

		List<TrafficModelDto> trafficModelDtos=new ArrayList<TrafficModelDto>();
		for(Api api:apis){
			TrafficModelDto dto=new TrafficModelDto();

			dto.setApiId(api.getId());
			dto.setName(api.getName());

			Traffic traffic=trafficService.findById(api.getId());
			dto.setRate(traffic.getRate());
			dto.setUnit(traffic.getUnit());

			trafficModelDtos.add(dto);
		}


		return trafficModelDtos;
	}

	public Project convertDto2Project(ProjectBasicDto dto){
		Project project=new Project();
		if (null!=dto.getName()){
			project.setName(dto.getName());
		}
		if (null!=dto.getDescription()){
			project.setDescription(dto.getDescription());
		}
		project.setStatus(dto.getStatus());
		project.setType(dto.getType());

		return project;

	}

}
