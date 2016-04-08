package com.enniu.qa.apm.model;

import org.ngrinder.model.BaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fuyong on 7/10/15.
 */
@Entity
@Table(name="t_commit")
public class Commit extends BaseModel<Commit>{

	private String message;
	private CommitType type;

	@Column(name = "commit_time")
	private Date commitTime;
	private String committor;

	@ManyToOne
	@JoinColumn(name = "project_id",referencedColumnName = "id")
	private Project project;

/*	@ManyToOne
	@JoinColumn(name = "api_id",referencedColumnName = "id")
	private API api;*/

	/*@OneToOne(mappedBy = "commit")
	private ApiTestRun run;
*/
	/*public ApiTestRun getRun() {
		return run;
	}

	public void setRun(ApiTestRun run) {
		this.run = run;
	}*/


	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CommitType getType() {
		return type;
	}

	public void setType(CommitType type) {
		this.type = type;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public String getCommittor() {
		return committor;
	}

	public void setCommittor(String committor) {
		this.committor = committor;
	}


}


