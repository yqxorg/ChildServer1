package com.zhuika.dao;

import com.watch.ejb.MqLog;
import com.watch.ejb.MqLogService;
import com.zhuika.util.ejbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * <p>Title: ejb title </p>
 * <p>Description: t_mq_log Client Dao 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-8-23 11:11:40
 */

public class MqLogDaoIml {	

  	public void AddMqLog(MqLog mqLog) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqLogService serviceClient;
		try {
			serviceClient = (MqLogService) weblogicContext.lookup("MqLogBean/remote");
			serviceClient.AddMqLog(mqLog);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public int GetMqLogCount(HashMap<String, String> map)
	{
		int total = 0 ;			
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqLogService serviceClient;
		try {
			serviceClient = (MqLogService)weblogicContext.lookup("MqLogBean/remote");
			total = serviceClient.GetMqLogCount(map);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return total;
	}
	

	public List<MqLog> ListMqLog(int offset, int length,HashMap<String, String> map)
	{		
		List<MqLog> listMqLog =  new ArrayList<MqLog>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqLogService serviceClient = (MqLogService)weblogicContext.lookup("MqLogBean/remote");
			listMqLog = serviceClient.ListMqLog(offset, length,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listMqLog;		
	}
    
    public MqLog findMqLog(String fid)
	{		
		MqLog mqLog = new MqLog();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqLogService serviceClient = (MqLogService)weblogicContext.lookup("MqLogBean/remote");
			mqLog = serviceClient.findMqLog(fid);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return mqLog;		
	}
    
  	public void UpdateMqLog(MqLog mqLog) {		
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqLogService serviceClient;
		try {
			serviceClient = (MqLogService)weblogicContext.lookup("MqLogBean/remote");
			serviceClient.UpdateMqLog(mqLog);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}        
   
	public void DeleteMqLog(String id) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqLogService serviceClient;
		try {
			serviceClient = (MqLogService) weblogicContext
					.lookup("MqLogBean/remote");
			serviceClient.DeleteMqLog(id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}