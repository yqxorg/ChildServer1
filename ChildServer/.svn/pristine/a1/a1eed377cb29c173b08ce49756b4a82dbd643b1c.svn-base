package com.zhuika.server;


import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteToMessageDec extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if (in.readableBytes() > 4) {	
			//System.out.println(ByteBufUtil.hexDump(in));	
//			System.out.println("in.maxCapacity():"+in.maxCapacity());
//			System.out.println(in.readableBytes());
			ByteBuf buf=in.readBytes(in.readableBytes());
			out.add(buf);
		}		
	}	
}
