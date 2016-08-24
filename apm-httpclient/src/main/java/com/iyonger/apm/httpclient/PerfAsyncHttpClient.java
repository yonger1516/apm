package com.iyonger.apm.httpclient;

import com.ning.http.client.AsyncHttpClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by fuyong on 1/14/16.
 */
public class PerfAsyncHttpClient implements PerfHttpClient {
	private final AsyncHttpClient asyncHttpClient;

	public PerfAsyncHttpClient(){
		this.asyncHttpClient=new AsyncHttpClient();
	}
	@Override
	public Response execute(Request request) {
		return null;
	}

	public long connectionTime(){
		return 0;
	}
}
