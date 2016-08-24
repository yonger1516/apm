package com.iyonger.apm.web.model;

import org.ngrinder.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by fuyong on 2/18/16.
 */
@Entity
@Table(name = "t_host")
public class Host extends BaseModel<Host>{

	String hostName;

	@Enumerated(EnumType.STRING)
	HostType type;

	String userName;
	String password;
	String sslKey;


	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public HostType getType() {
		return type;
	}

	public void setType(HostType type) {
		this.type = type;
	}


	public String getSslKey() {
		return sslKey;
	}

	public void setSslKey(String sslKey) {
		this.sslKey = sslKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
