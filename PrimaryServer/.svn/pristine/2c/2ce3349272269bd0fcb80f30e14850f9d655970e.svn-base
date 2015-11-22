package com.zhuika.server;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;

import java.util.concurrent.ExecutorService;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.IInfoDao;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.Info;
import com.zhuika.entity.SerialNumber;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelHandler.Sharable;

import org.apache.log4j.Logger;

/**
 * Handles a server-side channel.
 */
@Sharable
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {
	IInfoDao infoDao=DAOFactory.getInfoDao();
	ISerialNumberDao sndao = DAOFactory.getSerialNumberDao();
	static Logger logger = Logger.getLogger(DiscardServerHandler.class);
	private ExecutorService executorService;
	
	public DiscardServerHandler() {

	}

	public DiscardServerHandler(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			
			ctx.channel().close();
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf in = (ByteBuf) msg;
		
		byte[] bytes =in.array();		
		String sInfo = Hex.encodeHexStr(bytes);

		logger.info(String.format("primary received:%s", sInfo));			
		// 接收到包的长度
		int length = in.readableBytes();
		// 终端设备上报到服务器位始 $$
		String head = in.readBytes(2).toString(Charset.defaultCharset());
		
		if ("$$".equals(head)) {
			// 从包的字段得到包的长度
			short l = in.readShort();
			System.out.println(l);
			if(length==l){
				// 得到产品id 序列号
				final String serialNumber = byteToArray(in.readBytes(7).array());

				// 得到协议号
				final String agreement = byteToArray(in.readBytes(2).array());

				// 得到数据
				final String content = operation(serialNumber, head, length, l,
						agreement, in, ctx);
				// 得到校验位 checksum
				String checksum = byteToArray(in.readBytes(2).array());

				// 得到结束符\r\n
				final String end = byteToArray(in.readBytes(2).array());					
								
				executorService.execute(new Runnable() {
					public void run() {
						try {
							serviceHandler(ctx, serialNumber, agreement, content,
									end);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				});
				
			}							
		} 
		
	}

	protected void serviceHandler(ChannelHandlerContext ctx,
			String serialNumber, String agreement, String content, String end)
			throws Exception {
		/*
		* 子服务器的ip和端口(根据子服务器的ip和端口,可以做随机下发子服务器，然后设备根据你下发的信息,去连接子服务器)
		*/
		String ip="120.24.176.185";
		int port=8075;
		if ("0002".equals(agreement)) {
			String info = "40400023" + serialNumber + agreement
					+ Hex.encodeHexStr((ip+":"+port).getBytes());
			// 返回消息给终端
			returnMessage(info, ctx, end);
			Info in=infoDao.findBySerialNumber(serialNumber);
			if(in!=null){
				in.setIp(ip);
				in.setPort(port);
				infoDao.updateInfo(in);
			}else{
				Info i=new Info();
				i.setIp(ip);
				i.setPort(port);
				i.setSerialNumber(serialNumber);
				infoDao.addInfo(i);
			}
			if (sndao.findBySerialNumber(serialNumber) != null) {
				logger.info("SerialNumber has registered");
			} else {
				SerialNumber serialNum = new SerialNumber();
				serialNum.setSerialNumber(serialNumber);
				serialNum.setEf("3");
				sndao.addSerialNumber(serialNum);
				logger.info("new SerialNumber, SerialNumber added");
			}
		}		
	}

	
	private String operation(String serialNumber, String head, int length,
			short l, String agreement, ByteBuf in, ChannelHandlerContext ctx)
			throws ParseException {
		String content = null;
		if ("7101".equals(agreement) && length == l) {
			
			content = byteToArray(in.readBytes(l - 17).array());	
		} else if ("5100".equals(agreement) || "4106".equals(agreement)
				|| "9965".equals(agreement) || "3104".equals(agreement)
				|| "2001".equals(agreement) || "2002".equals(agreement)
				|| "2003".equals(agreement) || "9990".equals(agreement)
				|| "9992".equals(agreement) && length == l) {
			byteToArray(in.readBytes(1).array());			
			content = byteToArray(in.readBytes(l - 18).array());
			String info = new String(Hex.decodeHex(content.toCharArray()));
			System.out.println(content);
			System.out.println(info);			
		} else if ("9991".equals(agreement) && "$$".equals(head) && length == l) {

			content = byteToArray(in.readBytes(l - 17).array());
		} else if ("9982".equals(agreement) && "$$".equals(head) && length == l) {

			content = byteToArray(in.readBytes(l - 17).array());
		} else if ("4130".equals(agreement) && "$$".equals(head)) {

			byteToArray(in.readBytes(1).array());
			content = byteToArray(in.readBytes(l - 18).array());
		} else {	
			
			content = byteToArray(in.readBytes(l - 17).array());
			String info = new String(Hex.decodeHex(content.toCharArray()));
			System.out.println(content);
			System.out.println(info);			
		}
		return content;
	}	

	

	private void returnMessage(String info, ChannelHandlerContext ctx,
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
		
		logger.info(String.format("primary sent:%s", info + checksum + end));
		
		/*
		* 下发一次
		* 服务器下行指令 @@ 35 30020000000001 0002 120.24.176.185:6666 77A6 0D0A
		* l 35 2字节根据实际下发的字节长度去计算
		* 404000233002000000000100023132302E32342E3137362E3138353A3636363677A60D0A		
		*/
		ctx.writeAndFlush(b);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		// ctx.close();
	}

	public static String byteToArray(byte[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++) {
			result += Integer.toHexString((data[i] & 0xFF) | 0x100)
					.toUpperCase().substring(1, 3);
		}
		return result;
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
