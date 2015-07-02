package com.zhuika.service;

import org.apache.log4j.Logger;

import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.SerialNumber;
import com.zhuika.server.DiscardServerHandler;

public class OnLineService {
	private static ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();
	static Logger logger = Logger.getLogger(DiscardServerHandler.class);
	/**
	 * 设备下线
	 * @param key
	 */
	public static void getOffLine(String key) {
		try {
			SerialNumber serialNumber = sndao.findBySerialNumber(key);
			serialNumber.setOnline("0");
			sndao.updateSerialNumber(serialNumber);
			System.out.println("踢下线:" + key);
			logger.info("Yang kick off line:" + key);
		} catch (Exception e) {
			
		}		
	}
	
	public static void getOnLine(String serialNumber) {
		try {
			System.out.println("设备:" + serialNumber + "上线了");
			logger.info("yang device " + serialNumber + " getOnLine");
			SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
			serialNum.setOnline("1");
			sndao.updateSerialNumber(serialNum);
		} catch (Exception e) {
			
		}		
	}
}
