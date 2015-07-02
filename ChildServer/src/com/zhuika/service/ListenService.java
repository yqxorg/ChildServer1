package com.zhuika.service;

import com.zhuika.dao.DAOException;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.SerialNumber;
import com.zhuika.util.Hex;

public class ListenService {
	private static ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();	
	public static void getListen(String serialNumber, String content)
			throws DAOException {
		System.out.println("4130");
		SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
		String info = new String(Hex.decodeHex(content.toCharArray()));
		System.out.println(content);
		System.out.println(info);
		serialNum.setListenStatus("2");
		sndao.updateSerialNumber(serialNum);
	}
}
