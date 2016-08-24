package com.iyonger.apm.web.model;

import org.ngrinder.model.BaseModel;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by fuyong on 7/1/15.
 */
@Entity
@Table(name = "t_project")
public class Project extends BaseModel<Project>{

	private String name;

	private String description;

	@Enumerated(EnumType.STRING)
	private ProjectType type;
	private int status;

/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "config_id",referencedColumnName = "id",nullable = false)
	private ApiTestConfig apiTestConfig;*/

	/*@OneToMany(mappedBy = "project")
	private Set<Commit> commits;*/

	@OneToMany(mappedBy = "project")
	private Set<Api> apis;


	public Set<Api> getApis() {
		return apis;
	}

	public void setApis(Set<Api> apis) {
		this.apis = apis;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus(){
		return this.status;
	}

	public void setStatus(int status){
		this.status=status;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	@Override
	public String toString(){
		return "id:"+getId()+",name:"+name+",description:"+description+",type:"+type+",creator:"+getCreatedUser();
	}

}
