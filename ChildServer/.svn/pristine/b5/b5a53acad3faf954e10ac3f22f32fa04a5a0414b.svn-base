package com.zhuika.dao;


import com.users.ejb.SerialnumberApiphone;
import com.users.ejb.SerialnumberApiphoneService;

import com.zhuika.util.ejbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;



/**
 * <p>Title: ejb title </p>
 * <p>Description: t_serialnumber_apiphone Client Dao 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-9-5 21:34:34
 */

public class SerialnumberApiphoneDaoIml{	

  	public void AddSerialnumberApiphone(SerialnumberApiphone serialnumberApiphone) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberApiphoneService serviceClient;
		try {
			serviceClient = (SerialnumberApiphoneService) weblogicContext.lookup("SerialnumberApiphoneBean/remote");
			serviceClient.AddSerialnumberApiphone(serialnumberApiphone);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public int GetSerialnumberApiphoneCount(HashMap<String, String> map)
	{
		int total = 0 ;			
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberApiphoneService serviceClient;
		try {
			serviceClient = (SerialnumberApiphoneService)weblogicContext.lookup("SerialnumberApiphoneBean/remote");
			total = serviceClient.GetSerialnumberApiphoneCount(map);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return total;
	}
	

	public List<SerialnumberApiphone> ListSerialnumberApiphone(int offset, int length,HashMap<String, String> map)
	{		
		List<SerialnumberApiphone> listSerialnumberApiphone =  new ArrayList<SerialnumberApiphone>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberApiphoneService serviceClient = (SerialnumberApiphoneService)weblogicContext.lookup("SerialnumberApiphoneBean/remote");
			listSerialnumberApiphone = serviceClient.ListSerialnumberApiphone(offset, length,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listSerialnumberApiphone;		
	}
    
    public SerialnumberApiphone findSerialnumberApiphone(String fid)
	{		
		SerialnumberApiphone serialnumberApiphone = new SerialnumberApiphone();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberApiphoneService serviceClient = (SerialnumberApiphoneService)weblogicContext.lookup("SerialnumberApiphoneBean/remote");
			serialnumberApiphone = serviceClient.findSerialnumberApiphone(fid);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return serialnumberApiphone;		
	}
    
  	public void UpdateSerialnumberApiphone(SerialnumberApiphone serialnumberApiphone) {		
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberApiphoneService serviceClient;
		try {
			serviceClient = (SerialnumberApiphoneService)weblogicContext.lookup("SerialnumberApiphoneBean/remote");
			serviceClient.UpdateSerialnumberApiphone(serialnumberApiphone);	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}        
   
	public void DeleteSerialnumberApiphone(String id) {
		Context weblogicContext = ejbproxy.getInitialConnection();
		SerialnumberApiphoneService serviceClient;
		try {
			serviceClient = (SerialnumberApiphoneService) weblogicContext
					.lookup("SerialnumberApiphoneBean/remote");
			serviceClient.DeleteSerialnumberApiphone(id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}