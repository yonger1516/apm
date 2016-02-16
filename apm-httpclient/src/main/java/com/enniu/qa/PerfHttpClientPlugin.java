package com.enniu.qa;

import net.grinder.common.GrinderException;
import net.grinder.plugin.http.HTTPPlugin;
import net.grinder.plugininterface.*;
import net.grinder.script.Grinder;
import net.grinder.script.Statistics;
import net.grinder.statistics.StatisticsIndexMap;

/**
 * Created by fuyong on 1/20/16.
 */
public class PerfHttpClientPlugin implements GrinderPlugin {

	private static final HTTPPlugin s_singleton=new HTTPPlugin();
	static {
		try{
			PluginRegistry.getInstance().register(s_singleton);
		} catch (GrinderException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private PluginProcessContext m_pluginProcessContext;

	final PluginProcessContext getPluginProcessContext() {
		return m_pluginProcessContext;
	}

	@Override
	public void initialize(PluginProcessContext processContext) throws PluginException {

		m_pluginProcessContext = processContext;

		final Grinder.ScriptContext scriptContext =
				processContext.getScriptContext();

		// Register custom statistics.
		try {

			final Statistics statistics = scriptContext.getStatistics();

			statistics.registerDataLogExpression(
					"HTTP response code",
					StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_STATUS_KEY);

			statistics.registerDataLogExpression(
					"HTTP response length",
					StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_LENGTH_KEY);

			statistics.registerDataLogExpression(
					"HTTP response errors",
					StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_ERRORS_KEY);

			statistics.registerDataLogExpression(
					"Time to resolve host",
					StatisticsIndexMap.HTTP_PLUGIN_DNS_TIME_KEY);

			statistics.registerDataLogExpression(
					"Time to establish connection",
					StatisticsIndexMap.HTTP_PLUGIN_CONNECT_TIME_KEY);

			statistics.registerDataLogExpression(
					"Time to first byte",
					StatisticsIndexMap.HTTP_PLUGIN_FIRST_BYTE_TIME_KEY);

			statistics.registerSummaryExpression(
					"Mean response length",
					"(/ " + StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_LENGTH_KEY +
							" (+ (count timedTests) untimedTests))");

			statistics.registerSummaryExpression(
					"Response bytes per second",
					"(* 1000 (/ " + StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_LENGTH_KEY +
							" period))");

			statistics.registerSummaryExpression(
					"Response errors",
					StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_ERRORS_KEY);

			statistics.registerSummaryExpression(
					"Mean time to resolve host",
					"(/ " + StatisticsIndexMap.HTTP_PLUGIN_DNS_TIME_KEY +
							" (+ (count timedTests) untimedTests))");

			statistics.registerSummaryExpression(
					"Mean time to establish connection",
					"(/ " + StatisticsIndexMap.HTTP_PLUGIN_CONNECT_TIME_KEY +
							" (+ (count timedTests) untimedTests))");

			statistics.registerSummaryExpression(
					"Mean time to first byte",
					"(/ " + StatisticsIndexMap.HTTP_PLUGIN_FIRST_BYTE_TIME_KEY +
							" (+ (count timedTests) untimedTests))");
		}
		catch (GrinderException e) {
			throw new PluginException("Could not register custom statistics", e);
		}
	}

	@Override
	public PluginThreadListener createThreadListener(PluginThreadContext pluginThreadContext) throws PluginException {
		return new PerfHttpClientPluginThreadState(pluginThreadContext);
	}
}
