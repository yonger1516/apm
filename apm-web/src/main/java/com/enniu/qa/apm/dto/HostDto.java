package com.enniu.qa.apm.dto;

import com.enniu.qa.apm.model.HostType;
import net.grinder.message.console.AgentControllerState;

/**
 * Created by fuyong on 2/23/16.
 */
public class HostDto {

	long id=0l;
	String ip="";
	HostType type=HostType.GENERATOR;
	AgentControllerState status=AgentControllerState.UNKNOWN;
	String userName="";
	String password="";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HostType getType() {
		return type;
	}

	public void setType(HostType type) {
		this.type = type;
	}

	public AgentControllerState getStatus() {
		return status;
	}

	public void setStatus(AgentControllerState status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
