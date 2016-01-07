package com.enniu.qa.ptm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fuyong on 12/28/15.
 */
public class CommonUtils {

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	public static String getDateStr(){
		return sdf.format(new Date());
	}
}
