package net.grinder.http;

import HTTPClient.CookieModule;
import HTTPClient.DefaultAuthHandler;
import HTTPClient.HTTPConnection;
import net.grinder.common.GrinderException;
import net.grinder.common.TimeAuthority;
import net.grinder.plugininterface.GrinderPlugin;
import net.grinder.plugininterface.PluginException;
import net.grinder.plugininterface.PluginProcessContext;
import net.grinder.plugininterface.PluginThreadListener;
import net.grinder.script.Grinder;
import net.grinder.script.Statistics;
import net.grinder.statistics.StatisticsIndexMap;
import net.grinder.util.Sleeper;
import net.grinder.util.SleeperImplementation;


/**
 * Created by fuyong on 1/8/16.
 */
public class HTTPPlugin implements GrinderPlugin {

	private static HTTPPlugin s_singleton;

	/**
	 * Static package scope accessor for the initialised instance of the
	 * plug-in.
	 *
	 * @return The plug-in instance.
	 * @throws PluginException
	 */
	static final HTTPPlugin getPlugin() throws PluginException {
		synchronized (HTTPPlugin.class) {
			if (s_singleton == null) {
				throw new PluginException("Plugin has not been initialised");
			}

			return s_singleton;
		}
	}

	private final PluginProcessContext m_pluginProcessContext;
	private final Sleeper m_slowClientSleeper;
	private final Grinder.ScriptContext m_scriptContext;
	private final HTTPClient.HTTPConnection.TimeAuthority
			m_httpClientTimeAuthority;
	private boolean m_initialized;

	/**
	 * Constructor. Registered with the plugin container to be called at
	 * start up.
	 *
	 * @param processContext The plugin process context.
	 * @param scriptContext The script context.
	 */
	public HTTPPlugin(final PluginProcessContext processContext,
	                  final Grinder.ScriptContext scriptContext) {

		m_pluginProcessContext = processContext;
		m_scriptContext = scriptContext;

		final TimeAuthority timeAuthority = m_scriptContext.getTimeAuthority();

		m_slowClientSleeper = new SleeperImplementation(timeAuthority, null, 1, 0);

		m_httpClientTimeAuthority = new HTTPClient.HTTPConnection.TimeAuthority() {
			@Override public long getTimeInMilliseconds() {
				return timeAuthority.getTimeInMilliseconds();
			}
		};

		synchronized (HTTPPlugin.class) {
			s_singleton = this;
		}
	}

	final HTTPPluginThreadState getThreadState() throws GrinderException {
		return (HTTPPluginThreadState)
				m_pluginProcessContext.getPluginThreadListener(this);
	}

	final Grinder.ScriptContext getScriptContext() {
		return m_scriptContext;
	}

	/**
	 * Delay initialisation that is costly or has external effects until the
	 * plugin is used by the script.
	 *
	 * @throws PluginException if an error occurs.
	 */
	void ensureInitialised() throws PluginException {

		synchronized (this) {
			if (m_initialized) {
				return;
			}

			m_initialized = true;

			// Remove standard HTTPClient modules which we don't want. We load
			// HTTPClient modules dynamically as we don't have public access.
			try {
				// Don't want to retry requests.
				HTTPConnection.removeDefaultModule(
						Class.forName("HTTPClient.RetryModule"));
			}
			catch (final ClassNotFoundException e) {
				throw new PluginException("Could not load HTTPClient modules", e);
			}

			// Turn off cookie permission checks.
			CookieModule.setCookiePolicyHandler(null);

			// Turn off authorisation UI.
			DefaultAuthHandler.setAuthorizationPrompter(null);

			// Register custom statistics.
			try {

				final Statistics statistics = m_scriptContext.getStatistics();

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

				statistics.registerDataLogExpression(
						"New connections",
						StatisticsIndexMap.HTTP_PLUGIN_CONNECTIONS_ESTABLISHED);

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
								" (+ " + StatisticsIndexMap.HTTP_PLUGIN_CONNECTIONS_ESTABLISHED +
								"))");

				statistics.registerSummaryExpression(
						"Mean time to establish connection",
						"(/ " + StatisticsIndexMap.HTTP_PLUGIN_CONNECT_TIME_KEY +
								" (+ " + StatisticsIndexMap.HTTP_PLUGIN_CONNECTIONS_ESTABLISHED +
								"))");

				statistics.registerSummaryExpression(
						"Mean time to first byte",
						"(/ " + StatisticsIndexMap.HTTP_PLUGIN_FIRST_BYTE_TIME_KEY +
								" (+ (count timedTests) untimedTests))");
			}
			catch (final GrinderException e) {
				throw new PluginException(
						"Could not register custom statistics. Try importing HTTPRequest " +
								"from the top level of your script.", e);
			}
		}
	}

	/**
	 * Called by the engine to obtain a new PluginThreadListener.
	 *
	 * @return The new plug-in thread listener.
	 * @exception PluginException if an error occurs.
	 */
	@Override
	public PluginThreadListener createThreadListener() throws PluginException {

		return new HTTPPluginThreadState(m_scriptContext.getSSLControl(),
				m_slowClientSleeper,
				m_httpClientTimeAuthority);
	}

	// It may be useful to separate out a null implementation that can
	// be used without the core services. To do this, we should create
	// a separate interface, and default the singleton to use the null
	// implementation. It would be worth registering the underlying
	// script context components with pico: {sslControl, timeAuthority,
	// logger, statistics}, so we reduce the number of null implementations
	// required.
}
