package com.enniu.qa;


import retrofit.client.Request;
import retrofit.client.Response;

import java.lang.reflect.Method;

/**
 * Created by fuyong on 1/13/16.
 */
public class PerfHttpClientWrapper {
	PerfHttpClient httpClient;

	public PerfHttpClientWrapper(PerfHttpClient client){
		this.httpClient=client;
	}

	public Response execute(Method method, Request request){
		return null;
		//return httpClient.execute();
	}

}
