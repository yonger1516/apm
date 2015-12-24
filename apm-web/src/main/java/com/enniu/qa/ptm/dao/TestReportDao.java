package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.ApiTestRun;
import com.enniu.qa.ptm.model.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2015/8/12 0012.
 */
@Transactional
public interface TestReportDao extends JpaRepository<TestReport,Long>{
    TestReport findById(long id);
    TestReport findByApiTestRun(ApiTestRun run);
}
