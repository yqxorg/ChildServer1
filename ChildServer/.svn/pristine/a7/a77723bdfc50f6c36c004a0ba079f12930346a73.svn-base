package com.zhuika.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.zhuika.dao.DAOException;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.IPedoMeterDao;
import com.zhuika.entity.PedoMeter;
import com.zhuika.util.Hex;

public class PedometerService {
	private static IPedoMeterDao pedoMeterDao = DAOFactory.getIPedoMeterDao();
	public static void getPedoMeter(String serialNumber, String content)
			throws DAOException {
		System.out.println("7101");
		String info  = new String(Hex.decodeHex(content.toCharArray()));
		System.out.println(content);
		System.out.println(info);
		PedoMeter pedoMeter = pedoMeterDao.findBySerialNumber(serialNumber);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		String distance = "";
		if (pedoMeter == null) {
			System.out.println("11111111");
			String s[] = new String[7];
			s[day - 1] = info.trim() + ","
					+ (Math.round((Integer.parseInt(info.trim())) * (0.2)))
					+ "," + sdf.format(calendar.getTime());
			for (int i = 0; i < s.length; i++) {
				distance += s[i] + "/";
			}
			System.out.println("distance:" + distance);
			PedoMeter pm = new PedoMeter();
			pm.setSerialNumber(serialNumber);
			pm.setDistance(distance);
			pedoMeterDao.addPedoMeter(pm);
		} else if (pedoMeter != null) {
			System.out.println("222222");
			String s[] = pedoMeter.getDistance().split("/");
			s[day - 1] = info.trim() + ","
					+ (Math.round((Integer.parseInt(info.trim())) * (0.2)))
					+ "," + sdf.format(calendar.getTime());
			for (int i = 0; i < s.length; i++) {
				distance += s[i] + "/";
			}
			System.out.println("distance:" + distance);
			pedoMeter.setDistance(distance);
			pedoMeterDao.updatePedoMeter(pedoMeter);
		}
	}
}
