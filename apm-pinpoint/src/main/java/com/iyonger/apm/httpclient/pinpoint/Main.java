package com.iyonger.apm.httpclient.pinpoint;

import com.iyonger.apm.httpclient.pinpoint.data.ApdexUtil;
import com.iyonger.apm.httpclient.pinpoint.data.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fuyong on 2/16/16.
 */
public class Main {
	public static void main(String[] args){
		List<Request> requests=new ArrayList<Request>();

		for(int i=0;i<10;i++){
			Request r=new Request();
			r.setRt(new Random().nextInt(100));

			requests.add(r);
		}

		System.out.println("Apdex:"+ ApdexUtil.calApdex(requests));

	}
}
