package com.queue;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ProdAndConsWithML {

	
	public static void main(String[] args) {
		Connection connection = null;
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
		try {
			connection = connectionFactory.createConnection();
			System.out.println("Connection created");
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("Queue");
			MessageProducer messageProducer = session.createProducer(destination);
			MessageConsumer consumer = session.createConsumer(destination);
			connection.start();
			messageProducer.send(session.createTextMessage("Hi!"+new Date()));
			System.out.println("Message sent! ");
			TextMessage message = (TextMessage) consumer.receive();
			System.out.println("Message received! " + message.getText());
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
