package com.zhuika.server;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ActiveMqSender {

	private static final Logger logger = Logger.getLogger(ActiveMqSender.class);

	private Connection connection;
	private String subject = "Wearme.DEFAULTMQ";
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	public void startMQConn() throws JMSException {
		// ����connection
		connectionFactory = new ActiveMQConnectionFactory("failover:(tcp://localhost:61616)?timeout=1000");
		connection = connectionFactory.createConnection();
		connection.start();
		// ����session��������Ϣȷ�ϻ���
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// ����destination
		destination = session.createQueue(subject);
		// ����producer
		producer = session.createProducer(destination);
		// ����JMS�ĳ־���
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		
		//destination = session.createTopic("QualityQueue2");
	}

	private void send(String msg) {
		// ����connectionFaction
		try {
			// JMS��Ϣ��
//			msg += "filter=quality123";
			TextMessage message = session.createTextMessage(msg);			
			message.setObjectProperty("filter", "quality");
			
			// ������Ϣmessage
			producer.send(message);
			logger.info("mq msg sent,"+msg);
			// �ر���Դ
			message.clearProperties();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error("JMSException"+e.toString());
			try {
				this.close();
			} catch (Throwable ignore) {
			}
			e.printStackTrace();
		}
	}

	private void close() throws JMSException {
//		if (producer != null) {
//			producer.close();
//			producer=null;
//		}
		if (connection != null) {
			connection.stop();
			connection.close();
//			connection =null;
		}
//		if (session != null) {
//			session.close();
//			session =null;
//		}
		System.out.println("�ر���Դ��������");
	}

	
	public void sendToMq(String info)
	{
		//ActiveMqSender acSend = new ActiveMqSender();
		try {
			//acSend.startMQConn();
			
//			for(int i=0;i<50;i++){
				
			this.send(info);
//			
//			Thread.sleep(100);
//			}
			//acSend.close();
		} catch (Exception e) {
				// TODO Auto-generated catch block
			   logger.error("Exception"+e.toString());
				e.printStackTrace();
				
			}
	}
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		// ��������activeMQ
		// BrokerService broker = new BrokerService();
		// try {
		// broker.addConnector("tcp://localhost:61616");
		// broker.start();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		
		ActiveMqSender sender = new ActiveMqSender();		
		sender.sendToMq("data123");
	}
}

//http://alvinalexander.com/java/jwarehouse/activemq/activemq-core/src/test/java/org/apache/activemq/network/index.shtml