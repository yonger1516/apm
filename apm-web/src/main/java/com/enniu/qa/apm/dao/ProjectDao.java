package com.enniu.qa.apm.dao;

import com.enniu.qa.apm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Repository
public interface ProjectDao extends JpaRepository<Project,Long>{

    public Project findByName(String name);
    public Project findById(long id);

}
