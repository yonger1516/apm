package com.enniu.qa.apm.service;

import com.enniu.qa.apm.dao.ProjectDao;
import com.enniu.qa.apm.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fuyong on 7/1/15.
 */

@Service
public class ProjectService {

    @Autowired
    private ProjectDao dao;


    public List<Project> getAllProjects() {
        return dao.findAll();
    }

    public Project save(Project project) {
        if (project.exist()){
            Project exist=dao.findById(project.getId());
            project=exist.merge(project);
        }
        return dao.saveAndFlush(project);
    }

    public Project getProjectById(long id) {
        return dao.findById(id);
    }

    public void removeProject(long id) {
        dao.delete(id);
    }


    public void updateProject(Project project) {
        dao.saveAndFlush(project);
    }

}
