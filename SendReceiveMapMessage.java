package com.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SendReceiveMapMessage {

	/**
	 * @param args
	 */
	static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	
	 static Connection connection = null;
	public static void main(String[] args) {
		new SendReceiveMapMessage().sendReceive();
	}

	private void sendReceive() {
		MessageProducer messageProducer = null;
		MessageConsumer consumer = null;
		Session session = null;
		Destination dest = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			dest = session.createTopic("Topics");
			messageProducer = session.createProducer(dest);
			consumer = session.createConsumer(dest,("intValue=5"));
			MapMessage mapMessage = session.createMapMessage();
			int i = 903684,intProperty = 5;
			mapMessage.setInt("Value", i);
			mapMessage.setIntProperty("intValue", intProperty);
			messageProducer.send(mapMessage);
			connection.start();
			
			MapMessage map = (MapMessage) consumer.receive(100);
			
			System.out.println("Message Received "+map.getInt("Value"));
			System.out.println(" Boolean "+map.getBoolean("")+" double "+map.getDouble("d")+" byte "+map.getByte("")+" "+map.getInt("Value"));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
