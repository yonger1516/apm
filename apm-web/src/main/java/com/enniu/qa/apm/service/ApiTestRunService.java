package com.enniu.qa.apm.service;

import com.enniu.qa.apm.dao.ApiTestRunDao;
import com.enniu.qa.apm.model.ApiTestRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fuyong on 7/15/15.
 */
@Service
public class ApiTestRunService {

	@Autowired
	ApiTestRunDao dao;

	public ApiTestRun save(ApiTestRun run) {
		if (run.exist()){
			ApiTestRun exist=dao.findById(run.getId());
			run=exist.merge(run);
		}
		return dao.saveAndFlush(run);
	}

	public void deleteRun(long runId) {
		dao.delete(runId);
	}

	public ApiTestRun findByPerfTestId(long perfTestId){
		return dao.findByPerfTestId(perfTestId);
	}

	public ApiTestRun findByPerfTestName(String name){
		return dao.findByPerfTestName(name);
	}

	public ApiTestRun findById(long runId){
		return dao.findById(runId);
	}
}
