/*
package com.enniu.qa;

import HTTPClient.ModuleException;
import HTTPClient.ProtocolNotSuppException;
import HTTPClient.URI;
import net.grinder.common.GrinderException;
import net.grinder.plugin.http.HTTPPlugin;
import net.grinder.plugin.http.TimeoutException;
import net.grinder.plugin.http.URLException;
import net.grinder.plugininterface.PluginException;
import net.grinder.plugininterface.PluginProcessContext;
import net.grinder.plugininterface.PluginThreadContext;
import net.grinder.script.Grinder;
import net.grinder.script.InvalidContextException;
import net.grinder.script.Statistics;
import net.grinder.script.Test;
import net.grinder.statistics.StatisticsIndexMap;
import net.grinder.util.StreamCopier;
import org.slf4j.Logger;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;

import java.io.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static com.enniu.qa.HttpUtils.*;

*/
/**
 * Created by fuyong on 1/14/16.
 *//*

public class PerfHttpRequest {


	private static final Pattern s_pathParser =
			Pattern.compile("([^?#]*)(\\?([^#]*))?(#(.*))?");

	private static final Pattern s_absoluteURIPattern =
			Pattern.compile("^[^:/?#]*:.*");

	private volatile URI m_defaultURL;
	private volatile Header[] m_defaultHeaders = new Header[0];
	private volatile byte[] m_defaultData;
	private volatile Header[] m_defaultFormData;
	private volatile boolean m_readResponseBody = true;
	private PerfHttpClient perfHttpClient;

	*/
/**
	 * Creates a new <code>PerfHttpRequest</code> instance.
	 *//*

	public PerfHttpRequest() {
	}



	private abstract class AbstractRequest {
		private final URI m_url;
		private final Header[] m_mergedHeaders;

		public AbstractRequest(String uri,Header[] headers)
				throws URLException, HTTPClient.ParseException {

			final Header[] defaultHeaders = getHeaders();
			checkArray(defaultHeaders, "Default headers");

			if (defaultHeaders == headers) {
				m_mergedHeaders = defaultHeaders;
			}
			else {
				checkArray(headers, "headers");
				m_mergedHeaders = mergeArrays(getHeaders(), headers);
			}

			final URI defaultURL = m_defaultURL;

			if (uri == null) {
				if (defaultURL == null) {
					throw new URLException("URL not specified");
				}

				m_url = defaultURL;
			}
			else if (isAbsolute(uri)) {
				m_url = new URI(uri);
			}
			else {
				if (defaultURL == null) {
					throw new URLException("URL must be absolute");
				}

				if (uri.startsWith("//")) {
					// HTTPClient.URI(URI, String) treats paths that start with two
					// slashes as absolute. We don't want this, so handle as a special
					// case.
					final Matcher matcher = s_pathParser.matcher(uri);
					matcher.matches();
					final String path = matcher.group(1);
					final String query = matcher.group(2);
					final String fragment = matcher.group(3);

					m_url = new URI(defaultURL.getScheme(),
							defaultURL.getUserinfo(),
							defaultURL.getHost(),
							defaultURL.getPort(),
							path, query, fragment);
				}
				else {
					m_url = new URI(defaultURL, uri);
				}
			}
		}

		public final Response getHTTPResponse()
				throws GrinderException, IOException, ModuleException, ParseException,
				ProtocolNotSuppException {

			final PluginProcessContext pluginProcessContext =
					getPluginProcessContext();


			final PerfHttpClientPluginThreadState threadState = (PerfHttpClientPluginThreadState)
					pluginProcessContext.getPluginThreadListener();

			final PluginThreadContext threadContext = threadState.getThreadContext();

			boolean isAsync=pluginProcessContext.getScriptContext().getProperties().getBoolean("apm.perfhttpclient.isAsync",false);

			if (isAsync){
				perfHttpClient=new PerfAsyncHttpClient();
			}else{
				perfHttpClient=new PerfOKHttpClient();
			}


			final Grinder.ScriptContext scriptContext =
					pluginProcessContext.getScriptContext();


			final String pathAndQuery = m_url.getPathAndQuery();
			final String fragment = m_url.getFragment();

			final String path =
					fragment != null ? pathAndQuery + '#' + fragment : pathAndQuery;


			// This will be different to the time the Test was started if
			// the Test wraps several PerfHttpRequests.
			final long startTime =System.currentTimeMillis();

			final Response httpResponse;

			try {
				httpResponse = doRequest(perfHttpClient,path, m_mergedHeaders);
			}
			catch (InterruptedIOException e) {
				// We never interrupt worker threads, so we can be sure this is due to
				// a HTTPClient.
				throw new TimeoutException(e);
			}

			final long responseLength;

			if (m_readResponseBody) {
				// Read the entire response.
				// With standard HTTPClient, data is null <=> if Content-Length is 0.
				// We've modified HTTPClient to avoid this.

				responseLength = httpResponse.getBody().length()+httpResponse.getHeaders().size();
			}
			else {
				httpResponse.getStatus();
				responseLength = 0;
			}

			// Stop the clock whilst we do potentially expensive result processing.
			threadContext.pauseClock();

			*/
/*final long dnsTime = perfHttpClient.execute();
			final long connectTime = connection.getConnectTime();
			final long timeToFirstByte =
					httpResponse.getTimeToFirstByte() - startTime;*//*


			final int statusCode = httpResponse.getStatus();

			//add tracking id here
			String message = getTrackingId();
			message += httpResponse.getUrl() + " -> " + statusCode + " " +
					responseLength + " bytes";


			final Logger logger = scriptContext.getLogger();

			switch (statusCode) {
				case java.net.HttpURLConnection.HTTP_MOVED_PERM:
				case java.net.HttpURLConnection.HTTP_MOVED_TEMP:
				case 307:
					// It would be possible to perform the check automatically,
					// but for now just chuck out some information.
					logger.info(message +
							" [Redirect, ensure the next URL is " +getHeader(httpResponse.getHeaders(),"Location")
							 + "]");
					break;

				default:
					logger.info(message);
					break;
			}

			try {
				final Statistics statistics = scriptContext.getStatistics();

				if (statistics.isTestInProgress()) {
					// Log the custom statistics if we have a statistics context.

					final Statistics.StatisticsForTest statisticsForCurrentTest =
							statistics.getForCurrentTest();

					statisticsForCurrentTest.addLong(
							StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_LENGTH_KEY, responseLength);

					// If many PerfHttpRequests are wrapped in the same Test, the
					// last one wins.
					statisticsForCurrentTest.setLong(
							StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_STATUS_KEY, statusCode);

					// These statistics are accumulated over all the
					// PerfHttpRequests wrapped in the Test.
					*/
/*statisticsForCurrentTest.addLong(
							StatisticsIndexMap.HTTP_PLUGIN_DNS_TIME_KEY, dnsTime);

					statisticsForCurrentTest.addLong(
							StatisticsIndexMap.HTTP_PLUGIN_CONNECT_TIME_KEY, connectTime);

					statisticsForCurrentTest.addLong(
							StatisticsIndexMap.HTTP_PLUGIN_FIRST_BYTE_TIME_KEY,
							timeToFirstByte);*//*


					if (statusCode >= java.net.HttpURLConnection.HTTP_BAD_REQUEST) {
						statisticsForCurrentTest.addLong(
								StatisticsIndexMap.HTTP_PLUGIN_RESPONSE_ERRORS_KEY, 1);
					}
				}
			}
			catch (InvalidContextException e) {
				throw new PluginException("Failed to set statistic", e);
			}

			processResponse(httpResponse);
			threadState.setLastResponse(httpResponse);

			threadContext.resumeClock();

			return httpResponse;
		}

		abstract Response doRequest(PerfHttpClient client,
		                            String path,
		                            Header[] headers)
				throws IOException;



		protected String getTrackingId(){
			String track="[";
			for(Header header:m_mergedHeaders){
				if (header.getName().equals("X-Tracking-ID")){
					track+= header.getValue();
				}
			}
			track+="]";
			return track;
		}
	}

	private abstract class AbstractStreamingRequest extends AbstractRequest {

		public AbstractStreamingRequest(String uri, Header[] headers)
				throws HTTPClient.ParseException, URLException {
			super(uri, headers);
		}

		Response doRequest(PerfHttpClient perfHttpClient,
		                   String path,
		                   Header[] mergedHeaders)
				throws IOException {

			long contentLength = -1;

			for (int i = 0; i < mergedHeaders.length; ++i) {
				final Header header = mergedHeaders[i];
				if (header != null &&
						"Content-Length".equalsIgnoreCase(header.getName())) {
					contentLength = Long.parseLong(header.getValue());
					break;
				}
			}

			//TODO

			return null;
		}

		abstract InputStream getInputStream();

		abstract Response doStreamingRequest(PerfHttpClient perfHttpClient,
		                                     String path,
		                                     Header[] mergedHeaders,
		                                     OutputStream outputStream)
				throws IOException, ModuleException;
	}

	*/
/**
	 * Gets the default URL.
	 *
	 * @return The default URL to be used for this request, or
	 * <code>null</code> if the default URL has not been set.
	 *//*

	public final String getUrl() {
		final URI url = m_defaultURL;
		return url != null ? url.toString() : null;
	}

	
	public final void setUrl(String url) throws Exception {
		if (!isAbsolute(url)) {
			throw new URLException("URL must be absolute");
		}

		m_defaultURL = new URI(url);
	}

	*/
/**
	 * Gets the default headers.
	 *
	 * @return The default headers to be used for this request.
	 *//*

	public final Header[] getHeaders() {
		return m_defaultHeaders;
	}



	*/
/**
	 * Merges two Header arrays.
	 *
	 * @param defaultPairs
	 *          Default array.
	 * @param overridePairs
	 *          Array to merge. Entries take precedence over
	 *          <code>defaultPairs</code> entries with the same name.
	 * @return The merged arrays.
	 *//*

	private Header[] mergeArrays(Header[] defaultPairs, Header[] overridePairs) {

		final List<Header> result =
				new ArrayList<Header>(defaultPairs.length + overridePairs.length);

		final Set<String> seen = new HashSet<String>();

		for (Header p : overridePairs) {
			result.add(p);
			seen.add(p.getName());
		}

		for (Header p : defaultPairs) {
			if (!seen.contains(p.getName())) {
				result.add(p);
			}
		}

		return result.toArray(new Header[result.size()]);
	}

	
	public final void setHeaders(Header[] headers) {
		m_defaultHeaders = headers;
	}

	*/
/**
	 * Returns a string representation of the object and URL headers.
	 *
	 * @return a string representation of the object
	 *//*

	public String toString() {
		final StringBuilder result = new StringBuilder("");

		final URI url = m_defaultURL;

		if (url == null) {
			result.append("<Undefined URL>\n");
		}
		else {
			result.append(url.toString());
			result.append("\n");
		}

		final Header[] defaultHeaders = m_defaultHeaders;

		for (int i = 0; i < defaultHeaders.length; i++) {
			result.append(defaultHeaders[i].getName());
			result.append(": ");
			result.append(defaultHeaders[i].getValue());
			result.append("\n");
		}

		return result.toString();
	}

	*/
/**
	 * Gets the default data.
	 *
	 * @return The default data to be used for this request.
	 *//*

	public final byte[] getData() {
		return m_defaultData;
	}

	*/
/**
	 * Sets the default data.
	 *
	 * <p>See the {@link PerfHttpRequest warning above} regarding thread safety.
	 * Multiple worker threads that need to set specific data should either not
	 * share the same <code>PerfHttpRequest</code>, or pass the data as an argument
	 * to the call to <code>POST</code>, or <code>PUT</code>.</p>
	 *
	 * @param data The default data to be used for this request.
	 *//*

	public final void setData(byte[] data) {
		m_defaultData = data;
	}

	*/
/**
	 * Sets the default data from a file.
	 *
	 * <p>See the {@link PerfHttpRequest warning above} regarding thread safety.
	 * Multiple worker threads that need to set specific data should either not
	 * share the same <code>PerfHttpRequest</code>, or pass the data as an argument
	 * to the call to <code>POST</code>, or <code>PUT</code>. If the later is
	 * done, this method can still be used to read data from a file.</p>
	 *
	 * @param filename Path name of data file.
	 * @return The data read from the file.
	 * @throws IOException If the file could not be read.
	 *//*

	public final byte[] setDataFromFile(String filename) throws IOException {

		final File file = new File(filename);

		final ByteArrayOutputStream byteArrayStream =
				new ByteArrayOutputStream((int)file.length());

		new StreamCopier(4096, true).copy(
				new FileInputStream(file), byteArrayStream);

		final byte[] defaultData = byteArrayStream.toByteArray();

		m_defaultData = defaultData;

		return defaultData;
	}

	*/
/**
	 * Gets the default form data.
	 *
	 * @return The default form or query data to be used for this
	 * request.
	 *//*

	public final Header[] getFormData() {
		return m_defaultFormData;
	}

	*/
/**
	 * Sets the default form data.
	 *
	 * @param formData The default form or query data to be used for
	 * this request.
	 *//*

	public final void setFormData(Header[] formData) {
		m_defaultFormData = formData;
	}

	*/
/**
	 * Return whether or not the whole response body will be read.
	 *
	 * @return <code>true</code> => The response body will be read.
	 * @see #setReadResponseBody
	 *//*

	public boolean getReadResponseBody() {
		return m_readResponseBody;
	}


	public void setReadResponseBody(boolean b) {
		m_readResponseBody = b;
	}

	*/
/**
	 * Makes an HTTP <code>DELETE</code> request.
	 *
	 * @return Contains details of the servers response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response DELETE() throws Exception {
		return DELETE(null, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>DELETE</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response DELETE(String uri) throws Exception {
		return DELETE(uri, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>DELETE</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response DELETE(final String uri,final Header[] headers)
			throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(PerfHttpClient client,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException {

				Request request=new Request("DELETE",uri,Arrays.asList(headers),null);
				return client.execute(request);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>GET</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response GET() throws Exception {
		return GET((String)null);
	}

	*/
/**
	 * Makes an HTTP <code>GET</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response GET(String uri) throws Exception {
		return GET(uri, getFormData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>GET</code> request.
	 *
	 * @param queryData Request headers. Replaces all the values set
	 * by {@link #setFormData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response GET(Header[] queryData) throws Exception {
		return GET(null, queryData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>GET</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param queryData Request headers. Replaces all the values set
	 * by {@link #setFormData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response GET(final String uri, final Header[] queryData)
			throws Exception {
		return GET(uri, queryData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>GET</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param queryData
	 *          Request headers. Replaces all the values set by
	 *          {@link #setFormData}.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response GET(final String uri,
	                              final Header[] queryData,
	                              final Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(PerfHttpClient perfHttpClient,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException {

				//TODO
				Request request=new Request("GET",uri,Arrays.asList(headers),null);
				return perfHttpClient.execute(request);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>HEAD</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response HEAD() throws Exception {
		return HEAD(null, getFormData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>HEAD</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response HEAD(String uri) throws Exception {
		return HEAD(uri, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>HEAD</code> request.
	 *
	 * @param queryData Request headers. Replaces all the values set
	 * by {@link #setFormData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response HEAD(Header[] queryData) throws Exception {
		return HEAD(null, queryData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>HEAD</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param queryData Request headers. Replaces all the values set
	 * by {@link #setFormData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response HEAD(final String uri, final Header[] queryData)
			throws Exception {
		return HEAD(uri, queryData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>HEAD</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param queryData
	 *          Request headers. Replaces all the values set by
	 *          {@link #setFormData}.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response HEAD(final String uri,
	                               final Header[] queryData,
	                               Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Head(path, queryData, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response OPTIONS() throws Exception {
		return OPTIONS(null, getData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response OPTIONS(String uri) throws Exception {
		return OPTIONS(uri, getData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param data Data to be submitted in the body of the request.
	 * Overrides the value set with {@link #setData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response OPTIONS(final String uri, final byte[] data)
			throws Exception {
		return OPTIONS(uri, data, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param data
	 *          Data to be submitted in the body of the request. Overrides the
	 *          value set with {@link #setData}.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response OPTIONS(final String uri,
	                                  final byte[] data,
	                                  Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Options(path, mergedHeaders, data);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response OPTIONS(String uri, InputStream inputStream)
			throws Exception {
		return OPTIONS(uri, inputStream, getHeaders());
	}


	*/
/**
	 * Makes an HTTP <code>OPTIONS</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response OPTIONS(final String uri,
	                                  final InputStream inputStream,
	                                  Header[] headers) throws Exception {

		return new AbstractStreamingRequest(uri, headers) {
			InputStream getInputStream() {
				return inputStream;
			}

			Response doStreamingRequest(HTTPConnection connection,
			                                String path,
			                                Header[] mergedHeaders,
			                                HttpOutputStream outputStream)
					throws IOException, ModuleException {
				return connection.Options(path, mergedHeaders, outputStream);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST() throws Exception {
		return POST((String)null);
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(String uri) throws Exception {
		final byte[] data = getData();

		if (data != null) {
			return POST(uri, data, getHeaders());
		}
		else {
			return POST(uri, getFormData(), getHeaders());
		}
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param formData Data to be submitted as an
	 * <code>application/x-www-form-urlencoded</code> encoded request
	 * body.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(Header[] formData) throws Exception {
		return POST(null, formData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param formData Data to be submitted as an
	 * <code>application/x-www-form-urlencoded</code> encoded request
	 * body.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(String uri, Header[] formData)
			throws Exception {
		return POST(uri, formData, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param formData Data to be submitted as an
	 * <code>application/x-www-form-urlencoded</code> encoded request
	 * body.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(final String uri,
	                               final Header[] formData,
	                               Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Post(path, formData, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param formData Data to be submitted as an
	 * <code>application/x-www-form-urlencoded</code> or
	 * <code>multipart/form-data</code> encoded request
	 * body.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @param isMultipart
	 *          {@code true} if request type is multipart/form-data.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(final String uri,
	                               final Header[] formData,
	                               Header[] headers,
	                               boolean isMultipart) throws Exception {
		if (!isMultipart) {
			return POST(uri, formData, headers);
		}

		checkArray(headers, "POST - headers");

		final Header[] contentHeader = new Header[1];
		final byte[] data = Codecs.mpFormDataEncode(formData, null, contentHeader);

		return new AbstractRequest(uri, mergeArrays(headers, contentHeader)) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Post(path, data, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	private static void checkArray(Header[] headers, String context) {

		if (headers == null) {
			throw new NullPointerException(context + " is null");
		}

		for (int i = 0; i < headers.length; ++i) {
			if (headers[i] == null) {
				throw new NullPointerException(context + "[" + i + "] is null");
			}

			if (headers[i].getName() == null) {
				throw new NullPointerException(
						context + "[" + i + "].getName() is null");
			}
		}
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param data Data to be submitted in the body of the request.
	 * Overrides the value set with {@link #setData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(String uri, byte[] data) throws Exception {
		return POST(uri, data, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param data Data to be submitted in the body of the request.
	 * Overrides the value set with {@link #setData}.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(final String uri,
	                               final byte[] data,
	                               Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Post(path, data, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>POST</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(String uri, InputStream inputStream)
			throws Exception {
		return POST(uri, inputStream, getHeaders());
	}


	*/
/**
	 * Makes an HTTP <code>POST</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response POST(final String uri,
	                               final InputStream inputStream,
	                               Header[] headers) throws Exception {

		return new AbstractStreamingRequest(uri, headers) {
			InputStream getInputStream() {
				return inputStream;
			}

			Response doStreamingRequest(HTTPConnection connection,
			                                String path,
			                                Header[] mergedHeaders,
			                                HttpOutputStream outputStream)
					throws IOException, ModuleException {
				return connection.Post(path, outputStream, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response PUT() throws Exception {
		return PUT(null, getData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response PUT(String uri) throws Exception {
		return PUT(uri, getData(), getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param data Data to be submitted in the body of the request.
	 * Overrides the value set with {@link #setData}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response PUT(String uri, byte[] data) throws Exception {
		return PUT(uri, data, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param data
	 *          Data to be submitted in the body of the request. Overrides the
	 *          value set with {@link #setData}.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response PUT(final String uri,
	                              final byte[] data,
	                              Header[] headers) throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Put(path, data, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response PUT(String uri, InputStream inputStream)
			throws Exception {
		return PUT(uri, inputStream, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>PUT</code> request. This version allows the data
	 * to be passed as a stream, see the note in the
	 * {@link PerfHttpRequest class description}.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @param inputStream Data to be submitted in the body of the request.
	 * This stream will be fully read and closed when the method is called.
	 * The value set with {@link #setData} is ignored.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response PUT(final String uri,
	                              final InputStream inputStream,
	                              Header[] headers) throws Exception {

		return new AbstractStreamingRequest(uri, headers) {
			InputStream getInputStream() {
				return inputStream;
			}

			Response doStreamingRequest(HTTPConnection connection,
			                                String path,
			                                Header[] mergedHeaders,
			                                HttpOutputStream outputStream)
					throws IOException, ModuleException {
				return connection.Put(path, outputStream, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Makes an HTTP <code>TRACE</code> request.
	 *
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response TRACE() throws Exception {
		return TRACE(null, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>TRACE</code> request.
	 *
	 * @param uri The URI. If a default URL has been specified with
	 * {@link #setUrl}, this value need not be absolute and, if
	 * relative, it will be resolved relative to the default URL.
	 * Otherwise this value must be an absolute URL.
	 * @return Contains details of the server's response.
	 * @throws Exception If an error occurs.
	 *//*

	public final Response TRACE(String uri) throws Exception {
		return TRACE(uri, getHeaders());
	}

	*/
/**
	 * Makes an HTTP <code>TRACE</code> request.
	 *
	 * @param uri
	 *          The URI. If a default URL has been specified with {@link #setUrl},
	 *          this value need not be absolute and, if relative, it will be
	 *          resolved relative to the default URL. Otherwise this value must be
	 *          an absolute URL.
	 * @param headers
	 *          Request headers. Overrides headers with matching names set by
	 *          {@link #setHeaders}.
	 * @return Contains details of the server's response.
	 * @throws Exception
	 *              If an error occurs.
	 *//*

	public final Response TRACE(final String uri, Header[] headers)
			throws Exception {

		return new AbstractRequest(uri, headers) {
			Response doRequest(HTTPConnection connection,
			                       String path,
			                       Header[] mergedHeaders)
					throws IOException, ModuleException {
				return connection.Trace(path, mergedHeaders);
			}
		}
				.getHTTPResponse();
	}

	*/
/**
	 * Subclasses of PerfHttpRequest that wish to post-process responses
	 * should override this method.
	 *
	 * @param response The response.
	 *//*

	protected void processResponse(Response response) {
	}

	*/
/**
	 * Provide subclasses access to the process context.
	 *
	 * @return The process context.
	 *//*

	protected PluginProcessContext getPluginProcessContext() {
		return HTTPPlugin.getPlugin().getPluginProcessContext();
	}



	private static boolean isAbsolute(String uri) {
		return s_absoluteURIPattern.matcher(uri).matches();
	}

	private static Collection<String> s_httpMethodNames =
			asList("DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE");

	private static Test.InstrumentationFilter s_httpMethodFilter =
			new Test.InstrumentationFilter() {

				public boolean matches(Object item) {
					return s_httpMethodNames.contains(((Method)item).getName());
				}
			};

	*/
/**
	 * Return an instrumentation filter that selects only the HTTP request
	 * methods, i.e. {@code DELETE}, {@code GET}, and so on.
	 *
	 * @return An instrumentation filter that selects the HTTP request methods.
	 *//*

	public static Test.InstrumentationFilter getHttpMethodFilter() {
		return s_httpMethodFilter;
	}
}
*/
