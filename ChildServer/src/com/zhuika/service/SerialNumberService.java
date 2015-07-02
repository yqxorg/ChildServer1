package com.zhuika.service;

import com.zhuika.dao.DAOException;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.SerialNumber;
import com.zhuika.util.Hex;

public class SerialNumberService {
	private static ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();
	
	public static  void getDefaultInfo(String serialNumber, String content) throws DAOException {
		String info = new String(Hex.decodeHex(content.toCharArray()));
		String setGps = null;
		System.out.println(content);
		System.out.println(info);
		// gps_state=1,verno=F020.V1.004,interval=600,pwd=000000,sos_num1=13714366539,sos_num2=,sos_num3=,sos_num4=,,listen_num=,firewall=1,alm=0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;,prof=-1;0:0:-267970868;0:0:0;0:0:0;0:0:0;
		String[] s = info.split(",");
		System.out.println(s[2]);
		if (s[2].contains("=")) {
			int index = s[2].indexOf("=");
			setGps = s[2].substring(index + 1);
			System.out.println(setGps);
		}
		SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
		if (setGps == null) {
			// ����ȥ
			serialNum.setLbs("0");
			serialNum.setSetGps(setGps);
		} else if ("180".equals(setGps)) {
			serialNum.setLbs("1");
			serialNum.setSetGps(serialNum.getSetGps());
		} else {
			serialNum.setLbs("0");
			serialNum.setSetGps(setGps);
		}
		serialNum.setSerialNumber(serialNumber);
		serialNum.setEf("3");
		serialNum.setStatus("5");
		serialNum.setGpsStatus(serialNum.getGpsStatus());
		serialNum.setListenStatus("2");
		sndao.updateSerialNumber(serialNum);
	}
}
