package com.zhuika.util;

public class FNUtil {
	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI = 6.28318530712; // 2*PI
	static double DEF_PI180 = 0.01745329252; // PI/180.0
	static double DEF_R = 6370693.5; // radius of earth

	public static double GetShortDistance(double lon1, double lat1,
			double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 瑙掑害杞崲涓哄姬搴�
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 缁忓害宸�
		dew = ew1 - ew2;
		// 鑻ヨ法涓滅粡鍜岃タ缁�80 搴︼紝杩涜璋冩暣
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 涓滆タ鏂瑰悜闀垮害(鍦ㄧ含搴﹀湀涓婄殑鎶曞奖闀垮害)
		dy = DEF_R * (ns1 - ns2); // 鍗楀寳鏂瑰悜闀垮害(鍦ㄧ粡搴﹀湀涓婄殑鎶曞奖闀垮害)
		// 鍕捐偂瀹氱悊姹傛枩杈归暱
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	public static double GetLongDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 瑙掑害杞崲涓哄姬搴�
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 姹傚ぇ鍦嗗姡寮т笌鐞冨績鎵�す鐨勮(寮у害)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 璋冩暣鍒癧-1..1]鑼冨洿鍐咃紝閬垮厤婧㈠嚭
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 姹傚ぇ鍦嗗姡寮ч暱搴�
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离--yangqinxu
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double Distance(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	public static void main(String[] args) {
		double mLat1 = 22.542172; // point1绾害
		double mLon1 = 114.075752; // point1缁忓害
		double mLat2 = 22.594079;// point2绾害
		double mLon2 = 113.991287;// point2缁忓害
		double distance = GetLongDistance(mLon1, mLat1, mLon2, mLat2);
		System.out.println(distance);
		// 22.594079/113.991287
		// 22.594102/113.991292
		// 22.542172/114.075752
		// String location="22.542172/114.075752";
		// String[] l=location.split("/");
		// System.out.println(l[0]);

	}
}
