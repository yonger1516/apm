package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Api;
import com.enniu.qa.ptm.model.ApiRunTestConfig;
import com.enniu.qa.ptm.model.ApiTestConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by fuyong on 8/19/15.
 */
@Repository
public interface ApiTestConfigDao extends JpaRepository<ApiTestConfig,Long>{
	ApiTestConfig findById(long id);
	ApiTestConfig findByApiId(long apiId);

	void deleteByApi(Api api);

}
