package com.zhuika.server;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

import com.zhuika.dao.DAOException;
import com.zhuika.service.DownMessageService;
import com.zhuika.service.ListenService;
import com.zhuika.service.LocationService;
import com.zhuika.service.ManualLocationService;
import com.zhuika.service.OnLineService;
import com.zhuika.service.PedometerService;
import com.zhuika.service.SerialNumberService;
import com.zhuika.util.Hex;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * Handles a server-side channel.
 */
@Sharable
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {
	private static Map<String, Channel> map = new HashMap<String, Channel>();
	private ExecutorService executorService;	
	static Logger logger = Logger.getLogger(DiscardServerHandler.class);

	public DiscardServerHandler() {

	}

	public DiscardServerHandler(ExecutorService executorService) {
		this.executorService = executorService;
	}
    /**
     * 心跳处理
     */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			String key = null;
			if (map.containsValue(ctx.channel())) {
				for (Entry<String, Channel> entries : map.entrySet()) {
					if (entries.getValue() == ctx.channel()) {
						key = entries.getKey();
					}
				}
			} 
			if (key != null) {
				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String serialNumber = it.next();
					if (key.equals(serialNumber)) {
						it.remove();
						map.remove(serialNumber);						
						System.out.println("map.size():" + map.size());
						//判断设备下线
						OnLineService.getOffLine(key);						
					}
				}
			}
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

		logger.info(String.format("Child received:%s", sInfo));	
		
		// 接收到包的长度
		int length = in.readableBytes();
		// 终端设备上报到服务器位始 $$
		String head = in.readBytes(2).toString(Charset.defaultCharset());
		if ("$$".equals(head)) {
			// 从包的字段得到包的长度
			short l = in.readShort();
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(sdf.format(new Date()));				
				if (!map.containsKey(serialNumber)) {
					map.put(serialNumber, ctx.channel());
					//设备上线
					OnLineService.getOnLine(serialNumber);					
				}	
			}							
		} else if ("@@".equals(head)) {
			// 从包的字段得到包的长度
			short l = in.readShort();
			// 得到产品id 序列号
			final String serialNumber = byteToArray(in.readBytes(7).array());
			// 得到协议号
			final String agreement = byteToArray(in.readBytes(2).array());
			// 得到数据
			final String content = byteToArray(in.readBytes(l - 17).array());
			// 得到校验位 checksum
			String checksum = byteToArray(in.readBytes(2).array());
			// 得到结束符\r\n
			String end = byteToArray(in.readBytes(2).array());
			
			if (map.containsKey(serialNumber)) {
				executorService.execute(new Runnable() {					
					public void run() {
						System.out.println("123456123456");
						String info = "40400012" + serialNumber + agreement + content
								+ "0032" + "0d0a";
						byte b[] = HexString2Bytes(info);
//						System.out.println("b.toString:" + Arrays.toString(b));
//						System.out.println("b.length:" + b.length);
						
						logger.info(String.format("Yang @@ sent:%s", info));
						
						map.get(serialNumber).writeAndFlush(b);				
					}
				});				
			}
		}
	}

	protected void serviceHandler(ChannelHandlerContext ctx,
			String serialNumber, String agreement, String content, String end)
			throws Exception {
		if ("0001".equals(agreement)) {
			//下发心跳包
			DownMessageService.downMessage(ctx, serialNumber, agreement, end);
		} else if ("9991".equals(agreement)) {
			//下发时间给设备
			DownMessageService.downMessage(ctx, serialNumber, agreement, end);
			//根据9991，判断设备上线
			getOnLine(ctx, serialNumber, content);
		} else if ("9982".equals(agreement)) {
			//手动定位
			ManualLocationService.getRealTimePosition(serialNumber, content);
		} else if ("4130".equals(agreement)) {
			//监听
			ListenService.getListen(serialNumber, content);
		} else if ("7101".equals(agreement)) {
			//计步器
			PedometerService.getPedoMeter(serialNumber, content);
		} else if("7200".equals(agreement)){
			//设备下线
			getOffline(serialNumber);
		} else if("9955".equals(agreement)){
			//上传位置信息
			LocationService.getLocation(ctx, serialNumber, content, end);
		}
	}
	
	/**
	 * 通过上行的指令7200 判断设备下线	
	 * @param serialNumber
	 * @throws DAOException
	 */
	private void getOffline(String serialNumber) throws DAOException {
		if (map.containsKey(serialNumber)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (serialNumber.equals(key)) {
					it.remove();
					map.remove(serialNumber);
					System.out.println("踢下线:" + key);
					System.out.println("map.size():" + map.size());
					OnLineService.getOffLine(key);
				}
			}
		}
	}
    /**
     * 通过上行的指令9991 判断设备上线	
     * @param ctx
     * @param serialNumber
     * @param content
     * @throws DAOException
     */
	private void getOnLine(ChannelHandlerContext ctx, String serialNumber,
			String content) throws DAOException {
		SerialNumberService.getDefaultInfo(serialNumber, content);
		if (map.containsKey(serialNumber)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (serialNumber.equals(key)) {
					it.remove();
					map.remove(serialNumber);
				}
			}
		} else {
			map.put(serialNumber, ctx.channel());			
			System.out.println("map.size():" + map.size());
			System.out.println("99919991");
			System.out.println("设备:" + serialNumber + "上线了");
			
			logger.info("device:" + serialNumber + "getOnLine--");
			//设备上线
			OnLineService.getOnLine(serialNumber);			
		}
	}

	/**
	 * 根据上行的指令解析数据	
	 * @param serialNumber
	 * @param head
	 * @param length
	 * @param l
	 * @param agreement
	 * @param in
	 * @param ctx
	 * @return
	 * @throws DAOException
	 * @throws ParseException
	 */
	private String operation(String serialNumber, String head, int length,
			short l, String agreement, ByteBuf in, ChannelHandlerContext ctx)
			throws DAOException, ParseException {
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
}
