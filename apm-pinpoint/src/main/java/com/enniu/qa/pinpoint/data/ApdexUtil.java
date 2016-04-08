package com.enniu.qa.pinpoint.data;

import java.util.List;

/**
 * Created by fuyong on 2/16/16.
 */
public class ApdexUtil {

	public static double satisfiedLimit=60;//default is 60 ms

	public static double calApdex(List<Request> list){
		long satisfiedCount=0;
		long toleratedCount=0;
		long frustratedCount=0;

		for (Request r:list){
			double rt=r.getRt();
			//System.out.println("request with rt:"+rt);
			if (rt<=satisfiedLimit){
				satisfiedCount++;
			}else if(rt>satisfiedCount && rt<=4*satisfiedLimit){
				toleratedCount++;
			}else {
				frustratedCount++;
			}
		}

		return (satisfiedCount+(toleratedCount*0.5))/(satisfiedCount+toleratedCount+frustratedCount);
	}

}
