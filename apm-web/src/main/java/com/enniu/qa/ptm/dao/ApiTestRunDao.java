package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.RunSourceEnum;
import com.enniu.qa.ptm.model.ApiTestRun;
import org.ngrinder.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Repository
public interface ApiTestRunDao extends JpaRepository<ApiTestRun, Long>,JpaSpecificationExecutor<ApiTestRun> {

	public ApiTestRun findById(long id);

	public List<ApiTestRun> findByApiId(long apiId);

	public ApiTestRun findByPerfTestId(long perfTestId);
	public ApiTestRun findByPerfTestName(String name);

	/*@Modifying
	@Query("update ApiTestRun p set p.runningSample=?2, p.agentState=?3 where p.id=?1")
	int updateRuntimeStatistics(Long id, String runningSample, String agentState);

	@Modifying
	@Query("update ApiTestRun p set p.monitorState=?2 where p.id=?1")
	int updatetMonitorStatus(Long id, String monitorStatus);*/

	/*@Query("select r from t_run r left join t_test_config c on r.configId=c.id and r.status=?1 order by c.scheduled_time asc")
	List<TestRun> getAllByStatusAndScheduleTime(Status status);*/


	//List<TestRun> findAllByStatusAndRegionOrderByScheduledTimeAsc(Status status, String region);
}
