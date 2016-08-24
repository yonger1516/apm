package com.iyonger.apm.web.remotecontrol;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fuyong on 2/18/16.
 */
public class RemoteTask {
	String name;

	String targetId;
	String userName;
	String password;

	private Future<?> future;
	private BlockingQueue<String> outQueue        = new LinkedBlockingQueue<String>();

	private Vector<String> options         = new Vector<String>();

	SshServer sshServer;

	private final static long     TIMEOUT_DEFAULT = 10 * 60 * 1000;
	private long                  timeout         = TIMEOUT_DEFAULT;

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SshServer getSshServer() {
		return sshServer;
	}

	public void setSshServer(SshServer sshServer) {
		this.sshServer = sshServer;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	public BlockingQueue<String> getOutQueue() {
		return outQueue;
	}

	public void setOutQueue(BlockingQueue<String> outQueue) {
		this.outQueue = outQueue;
	}

	public Vector<String> getOptions() {
		return options;
	}

	public void setOptions(Vector<String> options) {
		this.options = options;
	}
}
