package cn.util;

public class StringUtil {
	public static String[] str2arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null;
	}
	
	public static String getDescString(String str){
		if(ValidateUtil.isValid(str) && str.length() > 30){
			return str.substring(0, 29);
		}
		return str;
	}
}
