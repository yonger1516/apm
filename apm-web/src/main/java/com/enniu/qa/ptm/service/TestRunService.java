package com.enniu.qa.ptm.service;

import com.enniu.qa.ptm.dao.ApiTestRunDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fuyong on 7/15/15.
 */
@Service
public class TestRunService {

	@Autowired
	ApiTestRunDao dao;



}
