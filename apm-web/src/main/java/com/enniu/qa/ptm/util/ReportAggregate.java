package com.enniu.qa.ptm.util;

import com.enniu.qa.ptm.model.TestReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fuyong on 7/17/15.
 */
public class ReportAggregate {

	public static double getAvgApdex(List<TestReport> reports){
		double sum=0;
		int i=1;
		for(TestReport report:reports){
			sum+=report.getApdex();
			i++;
		}
		return (double)sum/i;
	}

	public static double getAvgRT(List<TestReport> reports){
		double sum=0;
		int i=1;
		for(TestReport report:reports){
			sum+=report.getAvgRT();
			i++;
		}

		return (double)sum/i;
	}

	public static double getAvgTps(List<TestReport> reports){
		double sum=0;
		int i=1;
		for(TestReport report:reports){
			sum+=report.getAvgTps();
			i++;
		}

		return (double)sum/i;
	}

	public static double getAvgSdtDev(List<TestReport> reports){
		double sum=0;
		int i=1;
		for(TestReport report:reports){
			sum+=report.getSdtDev();
			i++;
		}

		return (double)sum/i;
	}

	public static double getMaxTps(List<TestReport> reports){
		List<Double> tps=new ArrayList<Double>();
		for(TestReport report:reports){
			tps.add(report.getMaxTps());
		}

		Collections.sort(tps);

		return (double)tps.get(tps.size()-1);
	}
}
