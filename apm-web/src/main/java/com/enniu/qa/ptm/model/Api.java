package com.enniu.qa.ptm.model;

import org.ngrinder.model.BaseModel;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by fuyong on 7/6/15.
 */
@Entity
@Table(name = "t_api")
public class Api extends BaseModel<Api> {

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id",nullable = false)
    private Project project;

    private String name;

    private String description;

    private int status;

    @OneToOne(mappedBy = "api")
    private ApiTestConfig apiTestConfig;

    @OneToMany(mappedBy = "api")
    @OrderBy("createdDate ASC")
    private Set<ApiTestRun> runs;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<ApiTestRun> getRuns() {
        return runs;
    }

    public void setRuns(Set<ApiTestRun> runs) {
        this.runs = runs;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /*public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }*/

    public ApiTestConfig getApiTestConfig() {
        return apiTestConfig;
    }

    public void setApiTestConfig(ApiTestConfig apiTestConfig) {
        this.apiTestConfig = apiTestConfig;
    }

   /* public TimeUnitEnum getUnit() {
        return unit;
    }

    public void setUnit(TimeUnitEnum unit) {
        this.unit = unit;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
