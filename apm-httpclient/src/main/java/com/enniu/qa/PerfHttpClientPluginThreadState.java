package com.enniu.qa;

import net.grinder.common.SkeletonThreadLifeCycleListener;
import net.grinder.plugininterface.PluginThreadContext;
import net.grinder.plugininterface.PluginThreadListener;
import retrofit.client.Response;

/**
 * Created by fuyong on 1/20/16.
 */
public class PerfHttpClientPluginThreadState extends SkeletonThreadLifeCycleListener implements PluginThreadListener {

	private final PluginThreadContext m_threadContext;
	private Response m_lastResponse;

	public PerfHttpClientPluginThreadState(PluginThreadContext threadContext){
		this.m_threadContext=threadContext;
	}

	public PluginThreadContext getThreadContext(){
		return m_threadContext;
	}

	public Response getLastResponse() {
		return m_lastResponse;
	}

	public void setLastResponse(Response m_lastResponse) {
		this.m_lastResponse = m_lastResponse;
	}
}
