package com.enniu.qa.apm.dao;

import com.enniu.qa.apm.model.Host;
import com.enniu.qa.apm.model.HostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fuyong on 2/18/16.
 */
@Repository
public interface HostDao extends JpaRepository<Host,Long>{

	public List<Host> findByType(HostType type);
	public Host findById(long id);

	/*@Modifying
	@Query("update Host h set h.status=?2 where h.id=?1")
	int updateStatus(long id,int status);
*/
}
