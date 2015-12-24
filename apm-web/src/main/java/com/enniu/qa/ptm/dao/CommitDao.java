package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Transactional
public interface CommitDao extends JpaRepository<Commit,Long>{
    public List<Commit> findByProjectId(long projectId);
    public Commit findById(long id);
}
