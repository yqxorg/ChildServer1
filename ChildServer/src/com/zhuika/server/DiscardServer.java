/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.zhuika.server;



import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.zhuika.entity.Config;
import com.zhuika.util.XMLReader;


/*
 * ��Netty 4��ʵ����һ���µ�ByteBuf�ڴ�أ�
 * ����һ����Java�汾�� jemalloc ��FacebookҲ���ã���
 * ���ڣ�Netty��������Ϊ������仺�������˷��ڴ�����ˡ�
 * ��������������������GC��������Ա��ҪС���ڴ�й©��
 * ��������ڴ����������ͷŻ���������ô�ڴ�ʹ���ʻ����޵�������
 * NettyĬ�ϲ�ʹ���ڴ�أ���Ҫ�ڴ����ͻ��˻��߷���˵�ʱ�����ָ�����������£�
 * .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
 * ʹ���ڴ��֮���ڴ��������ͷű���ɶԳ��֣�
 * ��retain()��release()Ҫ�ɶԳ��֣�����ᵼ���ڴ�й¶��
 * ֵ��ע����ǣ����ʹ���ڴ�أ�
 * ���ByteBuf�Ľ��빤��֮�������ʽ�ĵ���ReferenceCountUtil.release(msg)
 * �Խ��ջ�����ByteBuf�����ڴ��ͷţ��������ᱻ��Ϊ��Ȼ��ʹ���У������ᵼ���ڴ�й¶��
 */



/**
 * Discards any incoming data.
 */
public final class DiscardServer {

    
//    static final int PORT = Integer.parseInt(System.getProperty("port", "6666"));
  

    public static void main(String[] args) throws Exception {
    	
    	PropertyConfigurator.configure("log4j.properties");
    	
    	Logger logger = Logger.getLogger(DiscardServer.class); // ServerΪ����
    	  
    	logger.info("begin to start TCP server...");
    	
    	DiscardServer.run();
    }
    
	protected static void run() throws Exception {
		
	   EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             /*
              * AdaptiveRecvByteBufAllocator��������̬�����Ľ��ջ�������������
              * �������֮ǰChannel���յ������ݱ���С���м��㣬
              * ���������������ջ������Ŀ�д�ռ䣬��̬��չ������
              * �������2�ν��յ������ݱ���С��ָ��ֵ����������ǰ���������Խ�Լ�ڴ档
              * ���Ĭ��û�����ã���ʹ��AdaptiveRecvByteBufAllocator��
              */
             //.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
             //.handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     ChannelPipeline p = ch.pipeline(); 
                     p.addLast(new IdleStateHandler(0, 0, 150, TimeUnit.SECONDS));
                     p.addLast("framer",new DelimiterBasedFrameDecoder(8192, false,Delimiters.lineDelimiter()));
                     p.addLast(new ByteToMessageDec());
                     p.addLast(new MessageToByteEnc());             
                     p.addLast(ServiceHandlerFactory.getDiscardServerHandler());
                 }
             });
            
			Logger logger = Logger.getLogger(DiscardServer.class); // ServerΪ����
			Config config = XMLReader.loadconfig();
			String socketip = config.socketip;
			String socketport = config.socketport;
			
            // Bind and start to accept incoming connections.
//            ChannelFuture f = b.bind(PORT).sync();
            ChannelFuture f = b.bind(socketip, Integer.parseInt(socketport)).sync();		
            
            logger.info("TCP server started successfully");

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }		
	}
}