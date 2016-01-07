package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Api;
import com.enniu.qa.ptm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Repository
public interface ApiDao extends JpaRepository<Api,Long> {
    public List<Api> findByProjectId(long project);
    public Api findById(long id);
    public Api findByName(String name);
    public List<Api> findByStatus(int status);
}
