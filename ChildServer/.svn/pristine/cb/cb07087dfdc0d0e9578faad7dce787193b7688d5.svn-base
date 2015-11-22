package com.zhuika.util;

import java.util.HashMap;

import com.RestTest;

public class RestCall {

    private static final String acountid = "ab9abe2cacfc9a5a2829f2222993018a";
    private static final String token = "91b6bc5e40148c2a74335d2b730b8c2c";
    private static final String appid = "a8c88624ad064474a59225609832c505";
    
	public static String restCallBack(String fromNum,String toNum)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("18058149508", "68520027654483");
		map.put("13713975317", "68520027660803");
		
		String fromNumClient = map.get(fromNum);
		
		String[] args={"9","json","ab9abe2cacfc9a5a2829f2222993018a","91b6bc5e40148c2a74335d2b730b8c2c",
				"a8c88624ad064474a59225609832c505",fromNumClient,toNum,"","","","",""};
		String result = RestTest.testCallback(true, args[2], args[3], args[4], args[5], args[6],fromNum);	
		
		return result;
	}

	public static String restCallBack(String fromNum,String toNum,boolean useClientNum)
	{
		String fromNumClient = ejbproxy.getClientNumber(fromNum);
		
		if(fromNumClient==null || fromNumClient.equals(""))
		{
			return String.format("无对应的clientNumber,%s", fromNum);
		}
		
		String result = RestTest.testCallback(true, acountid, token, appid, fromNumClient, toNum,fromNum);	
		
		return result;
	}
	
	
	/**
	 * 发送短信，toNum：要发送的目标手机号码，templateid：短信要使用到的模板id（由云之迅平台定），如13567，
	 * @param templateid ，ex ：13567，13567-围栏短信模块id，模板内容：手表{1}在围栏{2}变化，状态为{3}
	 * @param toNum
	 * @param param，与模块id对应，ex，  1001,学校,离开
	 * @return
	 */
	public static String restSendSms(String templateid,String toNum,String param)
	{		
		String result = RestTest.testTemplateSMS(true, acountid, token, appid, templateid, toNum, param);
		
		return result;
	}
	
}
