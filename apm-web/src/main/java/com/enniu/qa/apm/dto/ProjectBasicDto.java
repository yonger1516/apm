package com.enniu.qa.apm.dto;

import com.enniu.qa.apm.model.ProjectType;

/**
 * Created by fuyong on 12/15/15.
 */
public class ProjectBasicDto {
	String name;
	String description;
	ProjectType type;

	int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}
}
