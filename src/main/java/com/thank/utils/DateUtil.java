package com.thank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private final static String DATE_FORMAT ="yyyy-MM-dd_HH:mm:ss";
    public static String getDateString(Date date)
    {
        if(date == null) {
            return "";
        }  
        SimpleDateFormat format =   new   SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }
    
    public static String getDateString(){
    	return getDateString(new Date());
    }
    
}
