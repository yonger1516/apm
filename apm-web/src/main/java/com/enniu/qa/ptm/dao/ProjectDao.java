package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */

@Transactional
public interface ProjectDao extends JpaRepository<Project,Long>{

    public Project findByName(String name);
    public Project findById(long id);

}
