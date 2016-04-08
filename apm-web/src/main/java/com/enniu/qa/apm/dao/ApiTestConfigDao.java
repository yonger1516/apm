package com.enniu.qa.apm.dao;

import com.enniu.qa.apm.model.Api;
import com.enniu.qa.apm.model.ApiTestConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fuyong on 8/19/15.
 */
@Repository
public interface ApiTestConfigDao extends JpaRepository<ApiTestConfig,Long>{
	ApiTestConfig findById(long id);
	ApiTestConfig findByApiId(long apiId);

	void deleteByApi(Api api);

}
