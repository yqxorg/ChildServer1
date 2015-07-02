package com.zhuika.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

public class CommonUtil {


    public static Date getNowDate() {  
  	  Date currentTime = new Date(); 
  	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
  	  String dateString = formatter.format(currentTime);  
  	  ParsePosition pos = new ParsePosition(8);  
  	  Date currentTime_2 = formatter.parse(dateString, pos);  
  	  return currentTime_2;  
  	}  
    
    public static Timestamp getTimeStamp()
    {
//    	Date date = new Date();
//    	Timestamp nousedate = new Timestamp(date.getTime());
//    	return nousedate;
    	
    	Timestamp nowdate1 = new Timestamp(System.currentTimeMillis());
    	return nowdate1;
    
    }
}
