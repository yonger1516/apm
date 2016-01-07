package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.ApiRunTestConfig;
import com.enniu.qa.ptm.model.ApiTestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by fuyong on 8/19/15.
 */
@Repository
public interface ApiRunTestConfigDao extends JpaRepository<ApiRunTestConfig,Long>{
	ApiRunTestConfig findById(long id);
	ApiTestRun findByApiTestRun(ApiTestRun run);
}
