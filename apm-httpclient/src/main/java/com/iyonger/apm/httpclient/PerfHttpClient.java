package com.iyonger.apm.httpclient;

import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by fuyong on 1/13/16.
 */
public interface PerfHttpClient {
	public Response execute(Request request);

}
