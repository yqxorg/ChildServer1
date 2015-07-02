package com.zhuika.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MinigpsUtil {

	public static void main(String[] args) {
		String lbsInfo="x=1cc-0-2495-f3d-96-2495-df5-8f-2495-f51-87-2495-f34-81-2495-df4-80-2495-1132-7f-2495-1309-7d&p=1&mt=1&ta=1";
		System.out.println(getLbsInfo(lbsInfo,""));
	}
	public static String getLbs(String mcc,String mnc,String lac,String cid){
		InputStream is=null;
 	    URL url;
 	    JSONObject data=new JSONObject();
 	    JSONArray jsonArr=new JSONArray();	   
 	    JSONObject lbs=new JSONObject();
 	    lbs.put("cell_id", cid);
 	    lbs.put("location_area_code", lac);
 	    lbs.put("home_mobile_country_code", mcc);
	    lbs.put("home_mobile_network_code", mnc);
	    lbs.put("age", 0);
	    jsonArr.add(lbs);
 	    data.put("version", "1.0.0");
 	    data.put("host","mapx.mapbar.com");
 	    data.put("access_token", "8a2ff951-c398-430d-8f48-53128693ede5");
 	    data.put("home_mobile_country_code", mcc);
 	    data.put("home_mobile_network_code", mnc);
 	    data.put("radio_type", "gsm");
 	    data.put("request_address", true);
 	    data.put("cell_towers",jsonArr);
 	    //System.out.println(data.toString());
 	    //System.out.println("http://mapx.mapbar.com/GeolocationPro/?data="+data);
		try {
			//http://mapx.mapbar.com/GeolocationPro/?data= {"version": "1.0.0","host": "mapx.mapbar.com","access_token": "8a2ff951-c398-430d-8f48-53128693ede5 ","home_mobile_country_code": 460,"home_mobile_network_code": 00,"radio_type": "gsm","request_address": "false",  "cell_towers": [{"cell_id": 37825, "location_area_code": 9539, "mobile_country_code": 460, "mobile_network_code": 01, "age": 0}]} 
			//data= {"version": "1.0.0","host": "mapx.mapbar.com","access_token": "8a2ff951-c398-430d-8f48-53128693ede5 ","home_mobile_country_code": 460,"home_mobile_network_code": 00,"radio_type": "gsm","request_address": "false",  
			//"cell_towers": [{"cell_id": 37825, "location_area_code": 9539, "mobile_country_code": 460, "mobile_network_code": 01, "age": 0}]} 
			url = new URL("http://mapx.mapbar.com/GeolocationPro/?data="+data);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
	        con.connect();
	        is=con.getInputStream();
	        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String str=reader.readLine();  
            //System.out.println(str);
            
            JSONObject json=JSONObject.fromObject(str);
     		String locationBd=JSONObject.fromObject(json.get("location")).get("longitude")+","+JSONObject.fromObject(json.get("location")).get("latitude");
     		///System.out.println(locationBd);
     		
     		double[] gcj2bd=BDTransUtil.gcj2bd(Double.parseDouble(locationBd.split(",")[1]), Double.parseDouble(locationBd.split(",")[0]));
     		//System.out.println("高德转百度:"+String.valueOf(gcj2bd[1]).substring(0, 9)+","+String.valueOf(gcj2bd[0]).substring(0, 9));
            str=String.valueOf(gcj2bd[1]).substring(0, 9)+","+String.valueOf(gcj2bd[0]).substring(0, 9);
     		return str;
		} catch (Exception e) {		
			e.printStackTrace();
		} finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static String getLbsInfo(String lac,String cid,String serialNumber){
		InputStream is=null;
 	    URL url; 	   
		try {
			url = new URL("http://222.76.219.175:99/api/geo/getlbs?key=aeb454a43f6a12a55ac25ea14cd2de0d&Lac="+lac+"&Cell="+cid);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
	        con.connect();
	        is=con.getInputStream();
	        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String str=reader.readLine(); 
            //System.out.println(serialNumber+"|"+url+"|"+str);
            //log.debug(serialNumber+"|"+"Lac="+lac+"&Cell="+cid+"|"+str);
            //{"Status":0,"Result":{"MCC":460,"MNC":1,"LAC":9539,"CELL":37825,"LNG":113.981483,"LAT":22.5878487,"O_LNG":113.986412,"O_LAT":22.5849171,"PRECISION":944,"ADDRESS":"广东省深圳市丽水路","DAY":20110315,"REGION":"广东省","CITY":"深圳市","COUNTRY":"中国"}}
            JSONObject json=JSONObject.fromObject(str);
            System.out.println(str);
            if(json.get("Status").equals(0)){
            	str=JSONObject.fromObject(json.get("Result")).get("LNG")+","+JSONObject.fromObject(json.get("Result")).get("LAT");          
//            	String str2=str=JSONObject.fromObject(json.get("Result")).get("O_LNG")+","+JSONObject.fromObject(json.get("Result")).get("O_LAT");          
//            	double[] gcj2bd=BDTransUtil.gcj2bd(Double.parseDouble(str2.split(",")[1]), Double.parseDouble(str2.split(",")[0]));
//            	System.out.println("高德转百度:"+String.valueOf(gcj2bd[1]).substring(0, String.valueOf(gcj2bd[1]).indexOf(".")+6)+","+String.valueOf(gcj2bd[0]).substring(0, String.valueOf(gcj2bd[0]).indexOf(".")+7));
            	System.out.println(str);
            }else{           
            	str=null;
            }
            return str;
		} catch (Exception e) {		
			e.printStackTrace();
		} finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	//String filePath="E:"+File.separator+"lszk"+File.separator+"30020000000001.txt";
	public static String[] getLbsInfo(String lbsInfo,String serialNumber){
		String filePath="F:"+File.separator+"lszk"+File.separator+"30020000000001.txt";
		InputStream is=null;
 	    URL url; 	   
		try {
			//url = new URL("http://minigps.net/as?"+lbsInfo);
			//使用中转url
			url = new URL("http://120.24.176.185:8082/web1/TransferUrl.action?http://minigps.net/as?"+lbsInfo);
			System.out.println("url:"+url);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.connect();
	        is=con.getInputStream();
	        BufferedReader reader=new BufferedReader(new InputStreamReader(is,"utf-8"));
            String str=reader.readLine(); 
            if("30020000000001".equals(serialNumber)){
            	 writerFile(filePath, url.toString(), true);
                 writerFile(filePath, str, true);
            }
           
            JSONObject json=JSONObject.fromObject(str);
            System.out.println(str);
            
            String address = "";
            if(json.get("cause").equals("OK")){
            	str=json.getString("lon")+","+json.getString("lat");   
            	address = json.getString("address");
            }else{           
            	str=null;
            }  
            String[] strArr = new String[2];
            strArr[0]=str;
            strArr[1]=address;
            
            return strArr;
            //return str;
		} catch (Exception e) {		
			e.printStackTrace();
		} finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	private static void writerFile(String filePath, String message, boolean flag) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				System.out.println("file1");
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;
			if (filePath != null && !"".equals(filePath)) {
				System.out.println("file2");
				osw = new OutputStreamWriter(new FileOutputStream(file, flag));
				bw = new BufferedWriter(osw);
			}
			if (message != null && !"".equals(message)) {
				bw.write(message);
				System.out.println("file3");
				bw.newLine();
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
