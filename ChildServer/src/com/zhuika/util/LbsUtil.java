package com.zhuika.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class LbsUtil {

	public static void main(String[] args) {
		//http://minigps.net/as?x=1cc-0-6212-2F8C-AC-52EC-28AD-96-6212-2F8B-96-6212-FAD-94-6212-3FB5-93-6212-FAB-93&p=1&mt=1&ta=1
		//String mcc="460";//new BigInteger(ss[0], 16).toString();//460
		//String mnc="01";//new BigInteger(ss[1],16).toString();//01
		String lac="9771";//new BigInteger(ss[2],16).toString();//9539
		String cid="4042";//new BigInteger(ss[3],16).toString();//37825
		//System.out.println(getLbs(mcc,mnc,lac,cid));
		//{"location":{"address":{"region":"广东省","county":"南山区","street":"桃源街道","street_number":"塘朗","city":"深圳市","country":"中国"},"longitude":"113.98598","latitude":"22.58925","accuracy":"1500"},"access_token":"8a2ff951-c398-430d-8f48-53128693ede5"}
//		JSONObject json=JSONObject.fromObject(getLbs(mcc,mnc,lac,cid));
//		String locationBd=JSONObject.fromObject(json.get("location")).get("longitude")+","+JSONObject.fromObject(json.get("location")).get("latitude");
//		System.out.println(locationBd);	
//		double[] gcj2bd=BDTransUtil.gcj2bd(Double.parseDouble(locationBd.split(",")[1]), Double.parseDouble(locationBd.split(",")[0]));
//		System.out.println("高德转百度:"+String.valueOf(gcj2bd[1]).substring(0, 9)+","+String.valueOf(gcj2bd[0]).substring(0, 9));
		String str=getLbsInfo(lac, cid,"55555555555555");
		System.out.println(str);
		double[] gcj = BDTransUtil.wgs2gcj(
				Double.parseDouble(str.split(",")[1]),
				Double.parseDouble(str.split(",")[0]));						
		String location = String.valueOf(gcj[1])
				.substring(0, String.valueOf(gcj[1]).indexOf(".")+6)
				+ ","
				+ String.valueOf(gcj[0]).substring(0, String.valueOf(gcj[0]).indexOf(".")+7);
		System.out.println("高德经纬度："+location);
		
		double[] bd=BDTransUtil.wgs2bd(Double.parseDouble(str.split(",")[1]),
				Double.parseDouble(str.split(",")[0]));
		String locationbd = String.valueOf(bd[1])
				.substring(0, String.valueOf(bd[1]).indexOf(".")+6)
				+ ","
				+ String.valueOf(bd[0]).substring(0, String.valueOf(bd[0]).indexOf(".")+7);
		System.out.println("百度经纬度:" + locationbd);
		
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
	public static String getLbsInfo(String lac,String cid){
		InputStream is=null;
 	    URL url; 	   
		try {
			url = new URL("http://222.76.219.175:99/api/geo/getlbs?key=aeb454a43f6a12a55ac25ea14cd2de0d&Lac="+Integer.parseInt(lac, 16)+"&Cell="+Integer.parseInt(cid, 16));
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.connect();
	        is=con.getInputStream();
	        BufferedReader reader=new BufferedReader(new InputStreamReader(is,"utf-8"));
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
	
}
