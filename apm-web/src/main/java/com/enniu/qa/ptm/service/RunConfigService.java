package com.enniu.qa.ptm.service;

import com.enniu.qa.ptm.dao.ApiRunTestConfigDao;
import com.enniu.qa.ptm.model.ApiRunTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.ngrinder.common.util.Preconditions.checkNotNull;

/**
 * Created by fuyong on 9/10/15.
 */

@Service
public class RunConfigService {
	@Autowired
	ApiRunTestConfigDao dao;

	public ApiRunTestConfig save(ApiRunTestConfig config){
		checkNotNull(config);
		return dao.saveAndFlush(config);
	}
}
