package com.zhuika.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageToByteEnc extends MessageToByteEncoder<Object> implements
		ChannelHandler {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {	
		byte[] b=(byte[]) msg;		
		out.writeBytes(b);
	}

}
