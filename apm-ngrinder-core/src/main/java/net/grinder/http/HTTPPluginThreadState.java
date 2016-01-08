package net.grinder.http;

import HTTPClient.*;
import net.grinder.common.SSLContextFactory;
import net.grinder.common.SkeletonThreadLifeCycleListener;
import net.grinder.plugininterface.PluginException;
import net.grinder.plugininterface.PluginThreadListener;
import net.grinder.util.Sleeper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuyong on 1/8/16.
 */
public class HTTPPluginThreadState extends SkeletonThreadLifeCycleListener implements PluginThreadListener {

	private final SSLContextFactory m_sslContextFactory;

	private final Map<URI, HTTPConnectionWrapper> m_httpConnectionWrappers =
			new HashMap<URI, HTTPConnectionWrapper>();
	private HTTPResponse m_lastResponse;
	private final Sleeper m_slowClientSleeper;
	private final HTTPConnection.TimeAuthority m_timeAuthority;

	HTTPPluginThreadState(final SSLContextFactory sslContextFactory,
	                      final Sleeper slowClientSleeper,
	                      final HTTPConnection.TimeAuthority timeAuthority)
			throws PluginException {
		m_sslContextFactory = sslContextFactory;
		m_slowClientSleeper = slowClientSleeper;
		m_timeAuthority = timeAuthority;
	}

	public HTTPConnectionWrapper getConnectionWrapper(final URI uri)
			throws ParseException,
			ProtocolNotSuppException,
			SSLContextFactory.SSLContextFactoryException {

		final URI keyURI =
				new URI(uri.getScheme(), uri.getHost(), uri.getPort(), "");

		final HTTPConnectionWrapper existingConnectionWrapper =
				m_httpConnectionWrappers.get(keyURI);

		if (existingConnectionWrapper != null) {
			return existingConnectionWrapper;
		}

		final HTTPPluginConnectionDefaults connectionDefaults =
				HTTPPluginConnectionDefaults.getConnectionDefaults();

		final HTTPConnection httpConnection = new HTTPConnection(uri);
		httpConnection.setContext(this);

		httpConnection.setSSLSocketFactory(
				m_sslContextFactory.getSSLContext().getSocketFactory());

		httpConnection.setTimeAuthority(m_timeAuthority);

		final HTTPConnectionWrapper newConnectionWrapper =
				new HTTPConnectionWrapper(httpConnection,
						connectionDefaults,
						m_slowClientSleeper);

		m_httpConnectionWrappers.put(keyURI, newConnectionWrapper);

		return newConnectionWrapper;
	}

	@Override
	public void beginRun() {
		// Discard our cookies.
		CookieModule.discardAllCookies(this);

		// Close connections from previous run.
		for (final HTTPConnectionWrapper connection :
				m_httpConnectionWrappers.values()) {
			connection.close();
		}

		m_httpConnectionWrappers.clear();
	}

	public void setLastResponse(final HTTPResponse lastResponse) {
		m_lastResponse = lastResponse;
	}

	public HTTPResponse getLastResponse() {
		return m_lastResponse;
	}
}
