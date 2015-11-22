package com.zhuika.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.common.ejb.EjbCmdData;
import com.users.ejb.EjbEnum.MqLogType;
import com.users.ejb.SerialnumberDetach;
import com.users.ejb.SerialnumberJpush;
import com.users.ejb.SerialnumberJpushService;
import com.watch.ejb.MqLog;
import com.watch.ejb.MqReceived;
import com.watch.ejb.MqTask;
import com.watch.ejb.Serialnumber;
import com.watch.ejb.SerialnumberService;
import com.zhuika.dao.MqLogDaoIml;
import com.zhuika.dao.MqReceivedDaoIml;
import com.zhuika.dao.MqTaskDaoIml;
import com.zhuika.dao.SerialnumberApiphoneDaoIml;
import com.zhuika.dao.SerialnumberDetachDaoIml;
import com.users.ejb.SerialnumberApiphone;


/**
 * @author yqx
 *
 */
public class ejbproxy {

	public static Context getInitialConnection() {
		final String INIT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
		final String SERVER_URL = "jnp://localhost:1099";
		Context ctx = null;
		try {
			Properties props = new Properties();

			props.put(Context.INITIAL_CONTEXT_FACTORY, INIT_FACTORY);
			props.put(Context.PROVIDER_URL, SERVER_URL);
			ctx = new InitialContext(props);
		} catch (NamingException ne) {
			// TODO: handle exception
			System.err.println("不能连接WebLogic Server在：" + SERVER_URL);
			ne.printStackTrace();
		}
		return ctx;
	}
	
//	public static void saveToMq(String snnumber,String cmd,String info,String usrid,String remark,String param)
//	{
//		try{
//			MqTask mqTaskInfo = new MqTask();
//			mqTaskInfo.setFcmd(cmd);
//			mqTaskInfo.setFsnid(snnumber);
//			mqTaskInfo.setFsenddata(info);
//			mqTaskInfo.setFuserid(usrid);
//			mqTaskInfo.setFtrycount(3);
//			mqTaskInfo.setFremark(remark);
//			mqTaskInfo.setFparam(param);
//			
//			MqTaskDaoIml mqDao = new MqTaskDaoIml();
//			
//			mqDao.AddMqTask(mqTaskInfo);
//		
//		
//		} catch (Exception ne) {
//			// TODO: handle exception
//			System.err.println("saveToMq error：" + ne.toString());
//			ne.printStackTrace();
//		}
//	}
	
	public static void saveToReceived(String snnumber,String cmd,String info,String usrid,String remark,String param)
	{
		try{
			
			MqReceived mqTaskInfo = new MqReceived();
			mqTaskInfo.setFcmd(cmd);
			mqTaskInfo.setFsnid(snnumber);
			mqTaskInfo.setFreceiveddata(info);
			mqTaskInfo.setFuserid(usrid);
			
			if(remark==null || remark.equals("")){
				String cmdName = EjbCmdData.getCmdNameByCmd(cmd);
				mqTaskInfo.setFremark(cmdName);
			}else
			{			
				mqTaskInfo.setFremark(remark);
			}
			
			MqReceivedDaoIml mqRevDao = new MqReceivedDaoIml();		
			mqRevDao.AddMqReceived(mqTaskInfo);
		
		} catch (Exception ne) {
			// TODO: handle exception
			System.err.println("saveToReceived error：" + ne.toString());
			ne.printStackTrace();
		}
	}
	
	public static void saveToLog(String snnumber,String cmd,String framedata,MqLogType logType,String escription,String param)
	{
		try{
			
			MqLog mqLog = new MqLog();
			mqLog.setFcmd(cmd);
			mqLog.setFsnnumber(snnumber);
			mqLog.setFcmdframe(framedata);
			mqLog.setFdescription(escription);
			mqLog.setFlogtype(logType.value());
			
			String cmdName = EjbCmdData.getCmdNameByCmd(cmd);
			mqLog.setFremark(cmdName);
			
			MqLogDaoIml mqLogvDao = new MqLogDaoIml();		
			mqLogvDao.AddMqLog(mqLog);
		
		} catch (Exception ne) {
			// TODO: handle exception
			System.err.println("saveToLog error：" + ne.toString());
			ne.printStackTrace();
		}
	}
	
	public static void saveDetach(String snnumber,String content)
	{
		try{
			
			SerialnumberDetach serialnumberDetach = new SerialnumberDetach();
			serialnumberDetach.setFdetachinfo(content);
			serialnumberDetach.setFsnid(snnumber);
			
			SerialnumberDetachDaoIml detachDao = new SerialnumberDetachDaoIml();		
			detachDao.AddSerialnumberDetach(serialnumberDetach);
		
		} catch (Exception ne) {
			// TODO: handle exception
			System.err.println("saveDetach error：" + ne.toString());
			ne.printStackTrace();
		}
	}
	
	/**
	 * 根据手机号码查找云之迅平台号码
	 * @param phoneNum
	 * @return
	 */
	public static String getClientNumber(String phoneNum)
	{
		HashMap<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("FMobile", phoneNum);
		
		SerialnumberApiphoneDaoIml apiPhoneDaoImpl =  new SerialnumberApiphoneDaoIml();
		
		//此处可建立缓存
		List<SerialnumberApiphone> listApi = apiPhoneDaoImpl.ListSerialnumberApiphone(0, 10000, mapCondition);
		
		if(listApi!=null && listApi.size()>0)
		{
			return listApi.get(0).getFclientnumber();
		}
		
		return null;
	}	
	

	/**
	 * 获取序列号信息
	 * @param snnumber
	 * @return
	 */
	public static List<Serialnumber> GetSerialNumberAll(String snnumber)
	{				
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("getIsReg", "1");
        map.put("sn.serialnumber", snnumber);      
        
		List<Serialnumber> listSerialnumber =  new ArrayList<Serialnumber>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberService serviceClient = (SerialnumberService)weblogicContext.lookup("SerialnumberBean/remote");
			listSerialnumber = serviceClient.ListSerialnumberAll(0, 100,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listSerialnumber;		
	}
	

	/**
	 * 获取用户的推送配置信息
	 * @param usrid
	 * @return
	 */
	public static List<SerialnumberJpush> ListSerialnumberJpush(String usrid)
	{		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FUserID", usrid);
		List<SerialnumberJpush> listSerialnumberJpush =  new ArrayList<SerialnumberJpush>();				
		try{
			Context weblogicContext = ejbproxy.getInitialConnection();			
			SerialnumberJpushService serviceClient = (SerialnumberJpushService)weblogicContext.lookup("SerialnumberJpushBean/remote");
			listSerialnumberJpush = serviceClient.ListSerialnumberJpush(0, 100,map);	

		  } catch (NamingException ne) {
			   // TODO: handle exception
			   System.err.println("不能连接NamingException在："+ne.toString());
			   ne.printStackTrace();
			  }
		
		return listSerialnumberJpush;		
	}
}
