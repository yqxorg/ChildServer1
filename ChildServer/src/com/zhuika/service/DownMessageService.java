package com.zhuika.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import com.zhuika.util.Hex;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

public class DownMessageService {
	
	static Logger logger = Logger.getLogger(DownMessageService.class);
	
	public static void downMessage(ChannelHandlerContext ctx, String serialNumber,
			String agreement, String end) {
		String info = "";
		if("0001".equals(agreement)){
			info = "40400012" + serialNumber + agreement + "01";
			//404000125555555555555500010124820D0A
		}else if("9991".equals(agreement)){
			Calendar cal = Calendar.getInstance();
			// 2、取得时间偏移量：
			int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
			// 3、取得夏令时差：
			int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
			// 、从本地时间里扣除这些差量，即可以取得UTC时间：
			cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
			info = "40400023" + serialNumber + agreement
					+ Hex.encodeHexStr(sdf.format(cal.getTime()).getBytes());			
		}
		//回拨应答
		else if("7105".equals(agreement)){
			info = "40400012" + serialNumber + agreement + "01";						
		}
		//脱落
		else if("7102".equals(agreement)){
			info = "40400012" + serialNumber + agreement + "01";						
		}
		// 返回消息给终端
		if(info!=""){
			returnMessage(info, ctx, end);
		}
		
	}
	
	private static void returnMessage(String info, ChannelHandlerContext ctx,
			String end) {
		byte[] b2 = HexString2Bytes(info);
		String checksum = Integer.toHexString(CRC_XModem(b2)).toUpperCase();
		System.out.println("checksum1:" + checksum);
		if (checksum.length() == 4) {

		} else if (checksum.length() == 3) {
			checksum = "0" + checksum;
		} else if (checksum.length() == 2) {
			checksum = "00" + checksum;
		} else if (checksum.length() == 1) {
			checksum = "000" + checksum;
		} else {
			checksum = checksum.substring(0, 4);
		}
		System.out.println("checksum2:" + checksum);
		byte b[] = HexString2Bytes(info + checksum + end);
//		System.out.println("b.toString:" + Arrays.toString(b));
//		System.out.println("b.length:" + b.length);
//		System.out.println("send_message:" + Hex.byteToArray(b));
	
		
		logger.info(String.format("Yang DownM sent:%s", info + checksum + end));
		/*
		* 
		*/
		ctx.writeAndFlush(b);
	}
	// 从十六进制字符串到字节数组转换
		private static byte[] HexString2Bytes(String hexstr) {
			byte[] b = new byte[hexstr.length() / 2];
			int j = 0;
			for (int i = 0; i < b.length; i++) {
				char c0 = hexstr.charAt(j++);
				char c1 = hexstr.charAt(j++);
				b[i] = (byte) ((parse(c0) << 4) | parse(c1));
			}
			return b;
		}

		private static int parse(char c) {
			if (c >= 'a')
				return (c - 'a' + 10) & 0x0f;
			if (c >= 'A')
				return (c - 'A' + 10) & 0x0f;
			return (c - '0') & 0x0f;
		}

		public static int CRC_XModem(byte[] bytes) {
			int crc = 0x00; // initial value
			int polynomial = 0x1021;
			for (int index = 0; index < bytes.length; index++) {
				byte b = bytes[index];
				for (int i = 0; i < 8; i++) {
					boolean bit = ((b >> (7 - i) & 1) == 1);
					boolean c15 = ((crc >> 15 & 1) == 1);
					crc <<= 1;
					if (c15 ^ bit)
						crc ^= polynomial;
				}
			}
			crc &= 0xffff;
			return crc;
		}
}
