package com.enniu.qa.apm.dao;

import com.enniu.qa.apm.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Repository
public interface CommitDao extends JpaRepository<Commit,Long>{
    public List<Commit> findByProjectId(long projectId);
    public Commit findById(long id);
}
