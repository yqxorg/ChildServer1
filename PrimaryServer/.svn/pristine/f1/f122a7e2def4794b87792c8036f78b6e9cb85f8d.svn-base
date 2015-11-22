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
    
	private static int ComputeCRCInt(byte[] val, int len)
	{
		
	    long crc;
	    long q;
	    byte c;
	    int i = 0;
		
	    crc = 0;
	    for (i = 0; i < len; i++)
	    {
	        c = val[i];
	        q = (crc ^ c) & 0x0f;
	        crc = (crc >> 4) ^ (q * 0x1081);
	        q = (crc ^ (c >> 4)) & 0xf;
	        crc = (crc >> 4) ^ (q * 0x1081);
	    }
	    return (byte)crc << 8 | (byte)(crc >> 8);
	}
	
	public static byte[] ComputeCRC(byte[] val, int len)
	{
		int d = ComputeCRCInt(val,len);
		
		byte[] result = new byte[2];   
		result[1] = (byte)((d >> 8) & 0xFF);
		result[0] = (byte)(d & 0xFF);
		
		return result;
	}
}
