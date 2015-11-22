package com.zhuika.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.IRtPositionDao;
import com.zhuika.entity.RtPosition;
import com.zhuika.util.BDTransUtil;
import com.zhuika.util.Hex;
import com.zhuika.util.MinigpsUtil;

public class ManualLocationService {
	private static IRtPositionDao rtpDao = DAOFactory.getIRtPositionDao();
	public static void getRealTimePosition(String serialNumber, String content)
			throws ParseException {
		RtPosition rtp = rtpDao.findBySerialNumber(serialNumber);
		System.out.println("9982");
		Calendar cal = Calendar.getInstance();
		Date nowTime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 经纬度内容
		String lon1 = null;
		String mlat1 = null;
		// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
		String info = new String(Hex.decodeHex(content.toCharArray()));
		if (info.contains("|")) {
			System.out.println("info:" + info);
			String[] ml = info.split("\\|");
			if (!"".equals(ml[0])) {
				String lat = ml[0].split(",")[2].replace(".", "");
				String lon = ml[0].split(",")[4].replace(".", "");
				System.out.println(lat);
				System.out.println(lon);
				lon1 = String.valueOf(Float.valueOf(lon.substring(0, 3))
						+ (Float.valueOf(lon.substring(3, 5)) / 60)
						+ (Float.valueOf(lon.substring(5)) / 600000));
				mlat1 = String.valueOf(Float.valueOf(lat.substring(0, 2))
						+ (Float.valueOf(lat.substring(2, 4)) / 60)
						+ (Float.valueOf(lat.substring(4)) / 600000));
				System.out.println(lon1 + "," + mlat1);
				if (lon1 != null) {
					// gps转百度坐标
					double[] bd = BDTransUtil.wgs2bd(Double.parseDouble(mlat1),
							Double.parseDouble(lon1));
					System.out.println("bd[0]:" + bd[0]);
					System.out.println("bd[1]:" + bd[1]);
					String lng = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6);
					lat = String.valueOf(bd[0]).substring(0,
							String.valueOf(bd[0]).indexOf(".") + 7);
					if (nowTime.getTime()
							- sdf.parse(rtp.getCreateTime()).getTime() < 150000) {
						System.out.println("456789");
						rtp.setLng(lng);
						rtp.setLat(lat);
						rtp.setQuery("1");
					} else {
						System.out.println("123456789");
						rtp.setLng(lng);
						rtp.setLat(lat);
						rtp.setQuery("0");
					}
					rtpDao.updateRtPosition(rtp);
				}
			} else {
				// 是基站定位都转换
				// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
				System.out.println(ml[3]);
				System.out.println("121212121212121212121");
//				String[] ss1 = ml[3].split(",");
//				String lac = new BigInteger(ss1[2], 16).toString();// 9384
//				String cid = new BigInteger(ss1[3], 16).toString();// 3643
//				System.out.println("lac:" + lac);
//				System.out.println("cid:" + cid);
				// 得到gps坐标
//				String lbsInfo = LbsUtil.getLbsInfo(lac, cid, serialNumber);
//				System.out.println("lbsInfo" + lbsInfo);
				String lbs=getLbsInfo(ml[3]);
				String lbsInfo=null;
			    if(lbs!=null){
			    	lbs="x="+lbs+"&p=1&mt=0&ta=1";
			    	String[] stringArr = null;
			    	//lbsInfo=MinigpsUtil.getLbsInfo(lbs,serialNumber);
			    	stringArr=MinigpsUtil.getLbsInfo(lbs,serialNumber);
			    	lbsInfo = stringArr[0];
			    }
				//String lbsInfo =getLbs(ml[3]);
				System.out.println("lbsInfo" + lbsInfo);
				if (lbsInfo != null) {
					double[] bd = BDTransUtil.wgs2bd(
							Double.parseDouble(lbsInfo.split(",")[1]),
							Double.parseDouble(lbsInfo.split(",")[0]));
					System.out.println("gps转百度:"
							+ String.valueOf(bd[1]).substring(0,
									String.valueOf(bd[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(bd[0]).substring(0,
									String.valueOf(bd[0]).indexOf(".") + 7));
					String lng = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6);
					String lat = String.valueOf(bd[0]).substring(0,
							String.valueOf(bd[0]).indexOf(".") + 7);
					System.out.println(nowTime.getTime());
					System.out
							.println(sdf.parse(rtp.getCreateTime()).getTime());
					System.out.println(nowTime.getTime()
							- sdf.parse(rtp.getCreateTime()).getTime());
					if (nowTime.getTime()
							- sdf.parse(rtp.getCreateTime()).getTime() < 300000) {
						System.out.println("456");
						rtp.setLng(lng);
						rtp.setLat(lat);
						rtp.setQuery("1");
					} else {
						System.out.println("789");
						rtp.setLng(lng);
						rtp.setLat(lat);
						rtp.setQuery("0");
					}
					rtpDao.updateRtPosition(rtp);
				}
			}
		}
	}
	private static String getLbsInfo(String lbs) {
		//1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71
		String msg[]=lbs.split(",");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<msg.length;i++){
			if(i>1&&Integer.parseInt(msg[i], 16)==0){
				break;
			}			
			sb=sb.append(msg[i]+"-");					
		}				
		if(sb.toString()!=null){
			return sb.toString().substring(0, sb.toString().length()-1);
		}
		return null;
	}
}
