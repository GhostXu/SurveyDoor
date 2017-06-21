package cn.util;

import java.util.Calendar;

public class LogUtil {

	public static String generateLogTableName(int i) {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1 ;
		int year = c.get(Calendar.YEAR) ;
		
		if((month + i) <= 0){
			return "log_" + (year - 1) + "_" + (month + 12 + i);
		}else{
			return "log_" + year + "_" + (month + i);
		}
	}

}
