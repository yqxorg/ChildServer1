package com.zhuika.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceHandlerFactory {
	private static ExecutorService executorService=Executors.newCachedThreadPool();
	private static DiscardServerHandler dsh=new DiscardServerHandler(executorService);
	public static DiscardServerHandler getDiscardServerHandler(){
		return dsh;	
	}
}
