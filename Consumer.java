package com.queue;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Consumer {

	private static String subject = "myQueue";
	
	public static void test(){
		Session session = null;
		Destination destination = null;
		try {
			final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
			final Connection connection = connectionFactory .createConnection();
			System.out.println("Connection : "+connection);
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(subject);
			
		/*	new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("Thread started!");
						try {
							Connection connection = connectionFactory .createConnection();
							Session threadSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
							
							MessageConsumer consumer = threadSession.createConsumer(threadSession.createQueue(subject));
							
							MessageListener listener = new MessageListener() {
								
								@Override
								public void onMessage(Message arg0) {
									try {
										System.out.println("Message recevied! ML ->"+((TextMessage) arg0).getText());
									} catch (JMSException e) {
										e.printStackTrace();
									}
									
								}
							};
							consumer.setMessageListener(listener);
						} catch (JMSException e) {
							e.printStackTrace();
						}
					
				}
			}).start();*/
			MessageConsumer consumer = session.createConsumer(destination);
			System.out.println("In Consumer...");
			TextMessage msg =null;
			//Verifying the status of the queue i.e, the messages present in the queue at this moment.
/*			QueueBrowser browser = session.createBrowser((Queue) destination);
			System.out.println("Looking into queue...");
			Enumeration enumeration = browser.getEnumeration();
			
			while(enumeration.hasMoreElements()){
				System.out.println("-> "+((TextMessage)enumeration.nextElement()).getText());
			}
			System.out.println("No more elements...");*/
			
			while((msg = (TextMessage) consumer.receiveNoWait())!=null){
				System.out.println("Message recevied! "+msg.getText());
			}
			
			//This is to check the messages in the queue after consumption.
		/*	enumeration = browser.getEnumeration();
			System.out.println("Looking into queue...");
			
			while(enumeration.hasMoreElements()){
				System.out.println("-> "+((TextMessage)enumeration.nextElement()).getText());
			}
			System.out.println("No more elements...");
			browser.close();*/
//			consumer.setMessageListener(this);
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} 

	}
public static void main(String[] args) {

	test();
	}
}
