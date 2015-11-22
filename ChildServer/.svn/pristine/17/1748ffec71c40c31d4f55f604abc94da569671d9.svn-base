package com.zhuika.dao;


import com.watch.ejb.MqTask;
import com.watch.ejb.MqTaskService;

import com.zhuika.util.ejbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;


/**
 * <p>Title: ejb title </p>
 * <p>Description: t_mq_task Client Dao 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-8-16 13:27:10
 */

public class MqTaskDaoIml  {	

  	public void AddMqTask(MqTask mqTask) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqTaskService serviceClient;
		try {
			serviceClient = (MqTaskService) weblogicContext.lookup("MqTaskBean/remote");
			serviceClient.AddMqTask(mqTask);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public int GetMqTaskCount(HashMap<String, String> map)
	{
		int total = 0 ;			
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqTaskService serviceClient;
		try {
			serviceClient = (MqTaskService)weblogicContext.lookup("MqTaskBean/remote");
			total = serviceClient.GetMqTaskCount(map);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return total;
	}
	

	public List<MqTask> ListMqTask(int offset, int length,HashMap<String, String> map)
	{		
		List<MqTask> listMqTask =  new ArrayList<MqTask>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqTaskService serviceClient = (MqTaskService)weblogicContext.lookup("MqTaskBean/remote");
			listMqTask = serviceClient.ListMqTask(offset, length,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listMqTask;		
	}
    
    public MqTask findMqTask(String fid)
	{		
		MqTask mqTask = new MqTask();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqTaskService serviceClient = (MqTaskService)weblogicContext.lookup("MqTaskBean/remote");
			mqTask = serviceClient.findMqTask(fid);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return mqTask;		
	}
    
  	public void UpdateMqTask(MqTask mqTask) {		
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqTaskService serviceClient;
		try {
			serviceClient = (MqTaskService)weblogicContext.lookup("MqTaskBean/remote");
			serviceClient.UpdateMqTask(mqTask);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}        
   
	public void DeleteMqTask(String id) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqTaskService serviceClient;
		try {
			serviceClient = (MqTaskService) weblogicContext
					.lookup("MqTaskBean/remote");
			serviceClient.DeleteMqTask(id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}