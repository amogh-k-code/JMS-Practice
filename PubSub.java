package com.topic;



import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class PubSub {
	
	static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	
	 static Connection connection = null;
	public static void main(String[] args) {
		PubSub publisher = new PubSub();
		publisher.produce();
		
	}
	private void produce() {
		System.out.println("In produce() ->");
		
		try {
			
			connection = connectionFactory.createConnection();
			connection.setClientID("UnqClntID1");
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createTopic("MyTopic");

			MessageProducer messageProducer = session.createProducer(dest);
			
			//filtering using property!
			TextMessage message = session.createTextMessage("Hi! intValue = 2"+new Date());
			
			message.setIntProperty("intValue", 2);
			message.setJMSCorrelationID(message.getJMSMessageID());
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			TextMessage message1 = session.createTextMessage("Hi! intValue = 1"+new Date());
			message1.setIntProperty("intValue", 1);
			message1.setJMSCorrelationID(message1.getJMSMessageID());
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

			TextMessage testMessage = session.createTextMessage("Hi! for DS's"+new Date());
			testMessage.setJMSCorrelationID(testMessage.getJMSMessageID());
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			MessageConsumer consumer1 = session.createConsumer(dest,("intValue=1"));
			System.out.println("Consumer1 created");
			
			MessageConsumer consumer2 = session.createConsumer(dest,("intValue=2"));
			System.out.println("Consumer2 created");
			MessageConsumer consumer3 = session.createDurableSubscriber((Topic) dest,"Subscriber1","(mapMessage=true)",false);
			System.out.println("Consumer3 created");
			MessageConsumer consumer4 = session.createConsumer(dest,("intValue=1"));
			System.out.println("Consumer4 created");
			messageProducer.send(message);
			messageProducer.send(message1);
			messageProducer.send(testMessage);
			
			testMapMessage(session,dest);
			
			System.out.println("Sent!");
			connection.start();
			while (true) {
				message1 = (TextMessage) consumer1.receiveNoWait();
				if (message1 != null) {
					System.out.println("Message1 received from consumer 1"
							+ message1.getText() + message1.getJMSMessageID());
				}
				else{
					break;
				}
			}
			while (true) {
				message1 = (TextMessage) consumer4.receiveNoWait();
				if (message1 != null) {
					System.out.println("Message1 received from consumer 4"
							+ message1.getText() + message1.getJMSMessageID());
				}
				else{
					break;
				}
			}
			while (true) {
				TextMessage message2 = (TextMessage) consumer2.receiveNoWait();
				if (message2 != null) {
					System.out.println("Message2 received from consumer 2"
							+ message2.getText() + message2.getJMSMessageID());
				} else {
					break;
				}
			}
			while (true) {
				Message message2 = consumer3.receiveNoWait();
				if (message2 != null) {
					if(message2 instanceof MapMessage){
						System.out.println("Message2 received from consumer 2"
								+ ((MapMessage) message2).getInt("MIN_VALUE"));
					}
					else if(message2 instanceof TextMessage){
						System.out.println("Message2 received from consumer 2"
								+ ((TextMessage) message2).getText() + message2.getJMSMessageID());
					}
				} else {
					break;
				}
				
			}
			consumer3.close();
			durableSubscriber(session,dest);
			/*System.out.println("Durable Consumer created");*/
			new DurableSubscriber().createDurableSubscriber(dest, connection);
			connection.close();
			System.out.println("Connection closed!");
		} catch (JMSException e) {
			e.printStackTrace();
		}  
	
	}
	private void testMapMessage(Session session, Destination dest) {
		MapMessage mapMessage = null;
		MessageProducer mP=null;
		System.out.println("Consumer3 created");
		try {
			mapMessage = session.createMapMessage();
			mapMessage.setBooleanProperty("mapMessage", true);
			mapMessage.setInt("MIN_VALUE", Integer.MIN_VALUE);
			mP = session.createProducer(dest);
			mP.send(mapMessage);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	private void durableSubscriber(Session session, Destination dest) {
		try {
			System.out.println("In ds()");
			MessageConsumer dconsumer = session.createDurableSubscriber((Topic) dest, "Subscriber2","(mapMessage=true)",false);
			System.out.println("Durable Subscriber created");
			while (true) {
				Message message3 =  dconsumer.receiveNoWait() ;
				if (message3 != null) {
					
					if (message3 instanceof TextMessage) {
						System.out.println("Message3 received "
								+ ((TextMessage) message3).getText()
								+ message3.getJMSMessageID());
					}
					else if(message3 instanceof MapMessage){
						System.out.println("Message3 received "
								+ ((MapMessage) message3).getInt("MIN_VALUE")
								+ message3.getJMSMessageID());
					}
				} else {
					System.out.println("Message is null");
					break;
				}
			}
			dconsumer.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}
