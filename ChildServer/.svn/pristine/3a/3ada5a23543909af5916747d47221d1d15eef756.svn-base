package com.zhuika.dao;

import com.watch.ejb.MqReceived;
import com.watch.ejb.MqReceivedService;
import com.zhuika.util.ejbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;


/**
 * <p>Title: ejb title </p>
 * <p>Description: t_mq_received Client Dao 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-8-16 18:56:03
 */

public class MqReceivedDaoIml {	

  	public void AddMqReceived(MqReceived mqReceived) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqReceivedService serviceClient;
		try {
			serviceClient = (MqReceivedService) weblogicContext.lookup("MqReceivedBean/remote");
			serviceClient.AddMqReceived(mqReceived);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public int GetMqReceivedCount(HashMap<String, String> map)
	{
		int total = 0 ;			
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqReceivedService serviceClient;
		try {
			serviceClient = (MqReceivedService)weblogicContext.lookup("MqReceivedBean/remote");
			total = serviceClient.GetMqReceivedCount(map);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return total;
	}
	

	public List<MqReceived> ListMqReceived(int offset, int length,HashMap<String, String> map)
	{		
		List<MqReceived> listMqReceived =  new ArrayList<MqReceived>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqReceivedService serviceClient = (MqReceivedService)weblogicContext.lookup("MqReceivedBean/remote");
			listMqReceived = serviceClient.ListMqReceived(offset, length,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listMqReceived;		
	}
    
    public MqReceived findMqReceived(String fid)
	{		
		MqReceived mqReceived = new MqReceived();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			MqReceivedService serviceClient = (MqReceivedService)weblogicContext.lookup("MqReceivedBean/remote");
			mqReceived = serviceClient.findMqReceived(fid);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return mqReceived;		
	}
    
  	public void UpdateMqReceived(MqReceived mqReceived) {		
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqReceivedService serviceClient;
		try {
			serviceClient = (MqReceivedService)weblogicContext.lookup("MqReceivedBean/remote");
			serviceClient.UpdateMqReceived(mqReceived);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}        
   
	public void DeleteMqReceived(String id) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		MqReceivedService serviceClient;
		try {
			serviceClient = (MqReceivedService) weblogicContext
					.lookup("MqReceivedBean/remote");
			serviceClient.DeleteMqReceived(id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}