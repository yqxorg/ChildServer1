package com.zhuika.dao;

import com.users.ejb.SerialnumberDetach;
import com.users.ejb.SerialnumberDetachService;
import com.zhuika.util.ejbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;


/**
 * <p>Title: ejb title </p>
 * <p>Description: t_serialnumber_detach Client Dao 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-8-31 14:36:43
 */

public class SerialnumberDetachDaoIml {	

  	public void AddSerialnumberDetach(SerialnumberDetach serialnumberDetach) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberDetachService serviceClient;
		try {
			serviceClient = (SerialnumberDetachService) weblogicContext.lookup("SerialnumberDetachBean/remote");
			serviceClient.AddSerialnumberDetach(serialnumberDetach);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public int GetSerialnumberDetachCount(HashMap<String, String> map)
	{
		int total = 0 ;			
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberDetachService serviceClient;
		try {
			serviceClient = (SerialnumberDetachService)weblogicContext.lookup("SerialnumberDetachBean/remote");
			total = serviceClient.GetSerialnumberDetachCount(map);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return total;
	}
	

	public List<SerialnumberDetach> ListSerialnumberDetach(int offset, int length,HashMap<String, String> map)
	{		
		List<SerialnumberDetach> listSerialnumberDetach =  new ArrayList<SerialnumberDetach>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberDetachService serviceClient = (SerialnumberDetachService)weblogicContext.lookup("SerialnumberDetachBean/remote");
			listSerialnumberDetach = serviceClient.ListSerialnumberDetach(offset, length,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listSerialnumberDetach;		
	}
    
    public SerialnumberDetach findSerialnumberDetach(String fid)
	{		
		SerialnumberDetach serialnumberDetach = new SerialnumberDetach();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberDetachService serviceClient = (SerialnumberDetachService)weblogicContext.lookup("SerialnumberDetachBean/remote");
			serialnumberDetach = serviceClient.findSerialnumberDetach(fid);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return serialnumberDetach;		
	}
    
  	public void UpdateSerialnumberDetach(SerialnumberDetach serialnumberDetach) {		
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberDetachService serviceClient;
		try {
			serviceClient = (SerialnumberDetachService)weblogicContext.lookup("SerialnumberDetachBean/remote");
			serviceClient.UpdateSerialnumberDetach(serialnumberDetach);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}        
   
	public void DeleteSerialnumberDetach(String id) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberDetachService serviceClient;
		try {
			serviceClient = (SerialnumberDetachService) weblogicContext
					.lookup("SerialnumberDetachBean/remote");
			serviceClient.DeleteSerialnumberDetach(id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}
