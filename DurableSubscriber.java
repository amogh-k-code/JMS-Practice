package com.topic;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;



public class DurableSubscriber {
	
	public void createDurableSubscriber(Destination topic, Connection connection){
		try {
			System.out.println("In Create() ->");
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createDurableSubscriber((Topic) topic, "Subscriber3","(mapMessage=true)",false);
			
			while (true) { 
				Message message =  consumer.receiveNoWait();
				if (message != null) {
					if (message instanceof TextMessage) {
						System.out.println("> Message4 Received "
								+ ((TextMessage) message).getText());
					}
					else if(message instanceof MapMessage){
						System.out.println("> Message4 Received "
								+ ((MapMessage) message).getInt("MIN_VALUE"));
					}
					
				} else {
					System.out.println("Message is null!");
					break;
				}
			}
			consumer.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
