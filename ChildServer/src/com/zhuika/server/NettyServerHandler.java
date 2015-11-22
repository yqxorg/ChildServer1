package com.zhuika.server;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.zhuika.entity.Msgpacket;
import com.zhuika.util.CommonUtil;


public class NettyServerHandler {
	 private static final Logger logger = Logger.getLogger(NettyServerHandler.class);	 
	 private static ConcurrentHashMap<String, HashSet<Msgpacket>> msgMapSet = new ConcurrentHashMap<String, HashSet<Msgpacket>>();
	  
	  private static ScheduledExecutorService executorService;


	  public static void SendRepeat() {

			if (executorService != null) {
				executorService.shutdown();
			}
			executorService = Executors.newScheduledThreadPool(1);
			executorService.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						
						SendFunction();
						
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
					}
				}
			}, 5, 5, TimeUnit.SECONDS);
		}
	  
	  private static void SendFunction()
	  {
//		  System.out.println("ͨ��Map.entrySet����key��value");
		  String key = null;
		  HashSet<Msgpacket> msgSet = null;
		  
		  for (Map.Entry<String, HashSet<Msgpacket>> entry : msgMapSet.entrySet()) {
			  key = entry.getKey();
			  msgSet = entry.getValue();
			  
			  if(msgSet!=null){
				  Iterator<Msgpacket> it = msgSet.iterator();  
				  while (it.hasNext()) {  
					  
					  Msgpacket msgpck = it.next();  			  
					  Timestamp timsstatmp = CommonUtil.getTimeStamp();
					  long diff = timsstatmp.getTime() - msgpck.getUpdatetime().getTime();
					  
					  //�������
					  long seconds = diff / (1000 ); 			  
					  //�����3���ط�
					  if(seconds>5&&msgpck.getRepeatCount()<=3)
					  {
						  msgpck.setRepeatCount(msgpck.getRepeatCount()+1);
						  msgpck.setUpdatetime(timsstatmp);
						  
						  //DiscardServerHandler.SendToClient(msgpck.getClientNum(),msgpck.getCharData(),msgpck.getMsgByte());					  
						  logger.info(String.format("�ط����ͻ��˱�ţ�%s,CMD��%s��data��%s��updatetime��%s��createtime��%s,diff:%s", msgpck.getClientNum(),msgpck.getCharData(),msgpck.getAck(),msgpck.getUpdatetime(),msgpck.getCreatetime(),seconds));
					  }	
					  //��ʱ�����ط���3�Σ��澯�쳣
					  else if(msgpck.getRepeatCount()>3)
						  {
							  logger.info(String.format("���ͳ�ʱ���ͻ��˱�ţ�%s,���ʹ�����%s��updatetime��%s��createtime��%s,diff:%s��CMD��%s��data��%s", msgpck.getClientNum(),msgpck.getRepeatCount(),msgpck.getUpdatetime(),msgpck.getCreatetime(),seconds,msgpck.getCmd(),msgpck.getCharData()));
							  
							  it.remove();
						  }	
				  }  
			  }
			  
			  if(msgSet.size()==0)
			  {
				  msgMapSet.remove(key);
			  }
			  //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		  }
		  

	  }
	  
	  //�յ�ACK��ɾ������
	  //����false��ʾ�Ѿ������ڣ��Ѿ��ɹ�ִ�У�����true��ʾӦ��û����
	  public static boolean DeleteSetForAck(String cliNum,String cmd,String ack,boolean isReceived)
	  {
		  //�������Ƿ���ڣ��粻���ڱ�ʾack�Ѿ��ش������ͽ�������
		  String clikey = cliNum;
		  if(msgMapSet.containsKey(clikey)){
			  HashSet<Msgpacket> msgSet = msgMapSet.get(clikey);			  
			  if(msgSet!=null){
				  Iterator<Msgpacket> it = msgSet.iterator();  
				  while (it.hasNext()) {  
					  Msgpacket msgpck = it.next();  
					  
//					  System.out.println(String.format("Received,cliNum:%s,cmd:%s,ack:%s:" ,cliNum,cmd,ack));
//					  System.out.println(String.format("Queue,cliNum:%s,cmd:%s,ack:%s:" ,msgpck.getClientNum(),msgpck.getCmd(),msgpck.getAck()));	
					  
					  if(msgpck.getAck().equals(ack) 
						 && msgpck.getClientNum().equals(cliNum)
						 && msgpck.getCmd().equals(cmd)){						  
						  //���ǽ��շ���������Ϣ��������ɾ��
						  if(isReceived){
							  it.remove();					  
							  logger.info(String.format("Remove cliNum:%s,cmd:%s,ack:%s:" ,cliNum,cmd,ack));
							  break;
						  }
						  else{
							  return true;
						  }						  					
					  }
				  }  
			  }
			  if(msgSet.size()==0){
				  msgMapSet.remove(clikey);
			  }
		  }
		  return false;		  
	  }
	  

	  
	  //���ӵ�mapset��clikey��sn number + ����� + ack
	  public static void AddToMapSet(String clientNum,String cmd,String ack,byte [] msgByte)
	  {		
		  Msgpacket packet = new Msgpacket();
		  packet.setAck(ack);
		  packet.setCmd(cmd);
		  packet.setClientNum(clientNum);
		  packet.setMsgByte(msgByte);
		  
		  Timestamp timsstatmp = CommonUtil.getTimeStamp();
		  packet.setCreatetime(timsstatmp);
		  packet.setUpdatetime(timsstatmp);		
		  
		  String clikey = clientNum;	 				  
		  if(!msgMapSet.containsKey(clikey))
		  {
			  HashSet<Msgpacket> msgSet =new HashSet<Msgpacket>();	
			 	  
			  msgSet.add(packet);			  
			  msgMapSet.put(clikey, msgSet);		
		  }	else
		  {			 			  
			  HashSet<Msgpacket> msgSet = msgMapSet.get(clikey);
			  msgSet.add(packet);
			 			  
		  }
	  }
}