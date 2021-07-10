package com.topic;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class LocalJMS {
	static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	
	public static void main(String[] args) {
		try {
			Connection con = connectionFactory.createConnection();
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("myTopic");
			
			MessageProducer mP = session.createProducer(topic);
			final MessageConsumer consumer = session.createConsumer(topic);
			
			mP.send(session.createTextMessage("Hi "+new Date()));
			System.out.println("Message sent!");
			/*consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message arg0) {
					System.out.println("In onMessage() -> ");
					try {
						System.out.println(((TextMessage) arg0).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});*/
			con.start();
			final TextMessage message = (TextMessage) consumer.receive();
			System.out.println(message.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
