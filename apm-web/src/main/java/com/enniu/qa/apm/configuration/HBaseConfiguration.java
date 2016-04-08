package com.enniu.qa.apm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by fuyong on 3/15/16.
 */

@Component
public class HBaseConfiguration {
	@Value("${hbase.client.host}")
	String host;

	@Value("${hbase.client.port}")
	int port;

	@Value("${hbase.ipc.client.tcpnodelay}")
	boolean tcpNoDelay;

	@Value("${hbase.rpc.timeout}")
	long rpcTimeOut;

	@Value("${hbase.client.operation.timeout}")
	long opsTimeOut;

	@Value("${hbase.ipc.client.socket.timeout.read}")
	long readTimeOut;

	@Value("${hbase.ipc.client.socket.timeout.write}")
	long writeTimeOut;

	@Value("${hbase.client.thread.max}")
	int maxThreads;

	@Value("${hbase.client.threadPool.queueSize}")
	int queueSize;

	@Value("${hbase.client.threadPool.prestart}")
	boolean preStart;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public long getRpcTimeOut() {
		return rpcTimeOut;
	}

	public void setRpcTimeOut(long rpcTimeOut) {
		this.rpcTimeOut = rpcTimeOut;
	}

	public long getOpsTimeOut() {
		return opsTimeOut;
	}

	public void setOpsTimeOut(long opsTimeOut) {
		this.opsTimeOut = opsTimeOut;
	}

	public long getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(long readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public long getWriteTimeOut() {
		return writeTimeOut;
	}

	public void setWriteTimeOut(long writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public boolean isPreStart() {
		return preStart;
	}

	public void setPreStart(boolean preStart) {
		this.preStart = preStart;
	}
}
