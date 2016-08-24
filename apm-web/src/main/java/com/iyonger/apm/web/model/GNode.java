package com.iyonger.apm.web.model;

import java.util.Date;

/**
 * Created by fuyong on 7/3/15.
 */
public class GNode {
	private int id;
	private String name;
	private int type;
	private String ip;
	private String user;
	private String password;
	private int status;
	private Date createTime;

	public GNode(){

	}
	public GNode(String name,int type,String IP,int status,Date createTime){
		this.name=name;
		this.type=type;
		this.ip=IP;
		this.status=status;
		this.createTime=createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
