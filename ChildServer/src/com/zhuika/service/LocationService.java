package com.zhuika.service;

import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.zhuika.dao.DAOException;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.ILocationInfoDao;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.LocationInfo;
import com.zhuika.entity.SerialNumber;
import com.zhuika.util.BDTransUtil;
import com.zhuika.util.Hex;
import com.zhuika.util.LbsUtil;
import com.zhuika.util.MinigpsUtil;

public class LocationService {	
	private static ILocationInfoDao lidao = DAOFactory.getILocationInfoDao();
	private static ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();
	public static void getLocation(ChannelHandlerContext ctx, String serialNumber,
			String content, String end) throws DAOException, ParseException {
		String lon1 = null;
		String mlat1 = null;
		SerialNumber serialNum=sndao.findBySerialNumber(serialNumber);
		String info = new String(Hex.decodeHex(content.toCharArray()));
		if (info.contains("|")) {
			System.out.println("info:" + info);
			String[] ml = info.split("\\|");
			String battery = null;
			if (ml.length == 8) {
				battery = ml[6];
			}
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
					// gps转高德坐标存入location字段
					double[] gcj = BDTransUtil.wgs2gcj(
							Double.parseDouble(mlat1),
							Double.parseDouble(lon1));
					String location = String.valueOf(gcj[1]).substring(0,
							String.valueOf(gcj[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(gcj[0])
									.substring(
											0,
											String.valueOf(gcj[0]).indexOf(
													".") + 7);
					System.out.println("高德经纬度:" + location);
					// gps转百度坐标
					double[] bd = BDTransUtil.wgs2bd(
							Double.parseDouble(mlat1),
							Double.parseDouble(lon1));
					String locationbd = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(bd[0]).substring(0,
									String.valueOf(bd[0]).indexOf(".") + 7);
					System.out.println("百度经纬度:" + locationbd);
					saveLocation(ctx, serialNumber, serialNum, end,
							location, locationbd, battery,"");
				}
			} else {
				// 不是基站定位10秒不转换
				if (!"1".equals(serialNum.getLbs())
						&& "10".equals(serialNum.getSetGps())) {

				} else {
					// 是基站定位都转换
					// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
					System.out.println(ml[3]);
					System.out.println("121212121212121212121");

					String lbs=getLbsInfo(ml[3]);					
					String lbsInfo=null;
					String Address = "";
					String[] stringArr = null;
				    if(lbs!=null){
				    	lbs="x="+lbs+"&p=1&mt=0&ta=1";
				    	
				    	stringArr=MinigpsUtil.getLbsInfo(lbs,serialNumber);
				    	lbsInfo = stringArr[0];
				    	Address =  stringArr[1];
				    	
				    	System.out.println("基站");
				    }
				    System.out.println("lbsInfo" + lbsInfo);
					String lbsInfo2 =getLbs(ml[3]);
					System.out.println("lbsInfo2" + lbsInfo2);					
					if (lbsInfo != null) {
						double[] bd = BDTransUtil.wgs2bd(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String locationbd = String.valueOf(bd[1]).substring(0,String.valueOf(bd[1]).indexOf(".") + 6)
								+ ","
								+ String.valueOf(bd[0]).substring(0,String.valueOf(bd[0]).indexOf(".") + 7);
						System.out.println("百度经纬度:" + locationbd);

						double[] gcj = BDTransUtil.wgs2gcj(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String location = String.valueOf(gcj[1]).substring(0, String.valueOf(gcj[1]).indexOf(".") + 6)+
								","+ String.valueOf(gcj[0]).substring(0,String.valueOf(gcj[0]).indexOf(".") + 7);
						System.out.println("高德经纬度：" + location);
						saveLocation(ctx, serialNumber, serialNum, end,
								location, locationbd, battery,Address);
					}
//                    if("30020000000001".equals(serialNumber)){
//                    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    	String filePath="F:"+File.separator+"lszk"+File.separator+"30020000000001.txt";
//						writerFile(filePath, ml[3], true);
//						writerFile(filePath, lbsInfo, true);
//						writerFile(filePath, lbsInfo2, true);
//						writerFile(filePath, sdf.format(new Date()), true);
//					}
					if (lbsInfo2 != null) {
						double[] bd = BDTransUtil.wgs2bd(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String locationbd = String.valueOf(bd[1]).substring(0,String.valueOf(bd[1]).indexOf(".") + 6)
								+ ","
								+ String.valueOf(bd[0]).substring(0,String.valueOf(bd[0]).indexOf(".") + 7);
						System.out.println("百度经纬度2:" + locationbd);						
					}
				}
			}
		}
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
	private static String getLbs(String lbs) {
		//1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71
		String msg[]=lbs.split(",");
		int count=0;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<msg.length;i++){
			i=i+2;
			if(Integer.parseInt(msg[i], 16)==0){
				break;
			}			
			String message=LbsUtil.getLbsInfo(msg[i], msg[i+1]);
			if(message!=null){
				sb=sb.append(message+":");
				count++;
			}
			if(count==3){
				break;
			}
		}
		String lbsInfo[]=sb.toString().split(":");
		if(lbsInfo.length==3){
			return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), Double.parseDouble(lbsInfo[1].split(",")[0]), Double.parseDouble(lbsInfo[2].split(",")[0]),3)
					+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), Double.parseDouble(lbsInfo[1].split(",")[1]), Double.parseDouble(lbsInfo[2].split(",")[1]),3);
		}else if(lbsInfo.length==2){
			return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), Double.parseDouble(lbsInfo[1].split(",")[0]), 0.0d,2)
					+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), Double.parseDouble(lbsInfo[1].split(",")[1]), 0.0d,2);
		}else{
			if(lbsInfo[0].length()>0){
				return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), 0.0d, 0.0d,1)
						+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), 0.0d, 0.0d,1);
			}
		}
		return null;				
	}
	public static double getAvg(double d1,double d2,double d3,int n){		
		return (d1+d2+d3)/n;	
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
	private static void saveLocation(ChannelHandlerContext ctx, String serialNumber,
			SerialNumber serialNum, String end, String location,
			String locationbd, String battery,String address) throws DAOException,
			ParseException {
		LocationInfo li = lidao.findBySeriaNumber(serialNumber);
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		// 设置一个时间点去触发修改存储的位置
		System.out.println(time);
		String mm = time.substring(14, 16);
		System.out.println("mm:" + mm);
		// 假如每个小时的30或者59分钟定到位就去查看是否超过7天了
		if ("30".equals(mm) || "59".equals(mm)) {
			System.out.println("ooo");
			if (li.getText() != null) {
				String[] ss = li.getText().split("/");
				System.out.println(ss.length);
				int a = 0;
				// 806400000 10天 保存用户7天的数据
				for (int k = 0; k < ss.length; k++) {
					if (date.getTime()
							- sdf.parse(ss[k].split(",")[2]).getTime() < 806400000) {
						System.out.println("k:" + k);
						if (k > 0) {
							System.out.println("222222");
							String s1 = ss[k - 1];
							System.out.println("s1" + s1);
							String s2 = li.getText().substring(
									li.getText().indexOf(s1) + s1.length() + 1);
							System.out.println("s2" + s2);
							String s3 = li.getText().substring(0,
									li.getText().indexOf(s1) + s1.length() + 1);
							System.out.println("s3:" + s3);

							li.setText(s2);
						}
						a++;
						if (a == 1) {
							break;
						}
					}
				}
			} else {
				li.setText(locationbd + "," + time);
			}
			if (li.getLocation() != null) {
				li.setLocation(li.getLocation() + "/" + location + "," + time);
				String[] ss = li.getLocation().split("/");
				System.out.println(ss.length);
				int a = 0;
				// 806400000 10天 保存用户7天的数据
				for (int k = 0; k < ss.length; k++) {
					if (date.getTime()
							- sdf.parse(ss[k].split(",")[2]).getTime() < 806400000) {
						System.out.println("k:" + k);
						if (k > 0) {
							System.out.println("222222");
							String s1 = ss[k - 1];
							System.out.println("s1" + s1);
							String s2 = li.getLocation().substring(
									li.getLocation().indexOf(s1) + s1.length()
											+ 1);
							System.out.println("s2" + s2);
							String s3 = li.getLocation().substring(
									0,
									li.getLocation().indexOf(s1) + s1.length()
											+ 1);
							System.out.println("s3:" + s3);
							li.setLocation(s2);
						}
						a++;
						if (a == 1) {
							break;
						}
					}
				}
			} else {
				li.setLocation(location + "," + time);
			}
		} else {
			if (li.getText() != null) {
				li.setText(li.getText() + "/" + locationbd + "," + time);
			} else {
				li.setText(locationbd + "," + time);
			}
			if (li.getLocation() != null) {
				li.setLocation(li.getLocation() + "/" + location + "," + time);
			} else {
				li.setLocation(location + "," + time);
			}
		}
		li.setLng(locationbd.split(",")[0]);
		li.setLat(locationbd.split(",")[1]);
		if (battery != null) {
			li.setBattery(battery);
			System.out.println("battery:" + battery);
		}
		lidao.update(li);
		// 短信模块
		SendSmsService.controlSMS(ctx, serialNumber, serialNum, locationbd, end, address);

	}		
}
