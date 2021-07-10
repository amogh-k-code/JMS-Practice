package com.queue;

import java.util.Date;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static String subject = "myQueue";
	public static void main(String[] args) {
		System.out.println("Started!");
		ConnectionFactory connectionFactory = (ConnectionFactory) new ActiveMQConnectionFactory(url);
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;
		TextMessage message = null;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			System.out.println("Connection : "+connection);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(subject);
			producer = session.createProducer(destination);
//			consumer = session.createConsumer(destination);

			message = session.createTextMessage("Hello World! 5 "+new Date());
			producer.send(message);nap();
			System.out.println("Message "+message.getText()+" sent! ");
			message = session.createTextMessage("Hello World! 6 "+new Date());
			producer.send(message);nap();
			System.out.println("Message "+message.getText()+" sent! ");
			message = session.createTextMessage("Hello World! 7 "+new Date());
			producer.send(message);nap();
			System.out.println("Message "+message.getText()+" sent! ");
			message = session.createTextMessage("Hello World! 8 "+new Date());
			producer.send(message);nap();
			message = session.createTextMessage("Hello World! 9 "+new Date());
			producer.send(message);nap();
			message = session.createTextMessage("Hello World! 10 "+new Date());
			producer.send(message);nap();
			System.out.println("Message "+message.getText()+" sent! ");
			nap();
			
			System.out.println("In producer : ");
			//This is to check the messages in the queue before consumption.
			QueueBrowser browser = session.createBrowser((Queue) destination);
			Enumeration enumeration = browser.getEnumeration();
			System.out.println("Looking into queue...");
			
			while(enumeration.hasMoreElements()){
				System.out.println("-> "+((TextMessage)enumeration.nextElement()).getText());
			}
			System.out.println("No more elements...");
			new Consumer().test();

			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private static void nap() throws InterruptedException {
		Thread.sleep(50);
	}
	

}
