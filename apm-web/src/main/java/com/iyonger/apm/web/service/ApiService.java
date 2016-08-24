package com.iyonger.apm.web.service;

import com.iyonger.apm.web.dao.ApiDao;
import com.iyonger.apm.web.model.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fuyong on 7/17/15.
 */
@Service
public class ApiService {
	@Autowired
	ApiDao dao;


	public Api save(Api api) {
		if (api.exist()){
			Api exist=dao.findById(api.getId());
			api=exist.merge(api);
		}
		return dao.saveAndFlush(api);
	}

	public void removeApi(long id) {
		dao.delete(id);
	}


	public Api findById(long apiId) {
		return dao.findById(apiId);
	}

	public List<Api> findByProjectId(long projectId) {
		return dao.findByProjectId(projectId);
	}

}

