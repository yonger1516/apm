package com.iyonger.apm.web.service;

import com.iyonger.apm.web.dao.TestReportDao;
import com.iyonger.apm.web.model.TestReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuyong on 7/15/15.
 */
@Service
public class TestReportService {

	@Autowired
	TestReportDao dao;

	public void addNewReport(TestReport report){
		dao.saveAndFlush(report);
	}

	public void deleteTestReport(long id){
		dao.delete(id);
	}

	public TestReport findById(long id){
		return dao.findById(id);
	}

	public List<TestReport> findByIds(List<Long> ids){
		List<TestReport> reports=new ArrayList<TestReport>();

		for(Long id:ids){
			reports.add(findById(id));
		}
		return reports;
	}

	public TestReport save(TestReport report){
		return dao.saveAndFlush(report);
	}
}
