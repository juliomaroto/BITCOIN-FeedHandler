package org.feedhandler.sender;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FeedSender {
	private final String HEARTBEAT_KEYWORD_RESPONSE = "hb";
	
	private String rabbitChannel;
	private HashMap<String, String> rabbitMqConnectionProperties;
	private Channel channel;
	private Connection connection;
	
	public FeedSender(HashMap<String, String> rabbitMqConnectionProperties) {
		this.rabbitMqConnectionProperties = rabbitMqConnectionProperties;
		this.rabbitChannel = rabbitMqConnectionProperties.get("channel");
	}

	public void connect() {
		ConnectionFactory factory = new ConnectionFactory();
		setConnectionProperties(factory);
		
		setConnection(factory);
		setCommunicationChannel();
		setRabbitQueueForChannel();
	}
	
	private void setConnectionProperties(ConnectionFactory factory) {
		factory.setHost(rabbitMqConnectionProperties.get("host"));
		factory.setUsername(rabbitMqConnectionProperties.get("username"));
		factory.setPassword(rabbitMqConnectionProperties.get("password"));
	}
	
	private void setCommunicationChannel() {				
		try {
			this.channel = connection.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setRabbitQueueForChannel() {
		try {
			channel.queueDeclare(rabbitChannel, false, false, false, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		try {
			if (!message.contains(HEARTBEAT_KEYWORD_RESPONSE)) {
				channel.basicPublish("", rabbitChannel, null, message.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setConnection(ConnectionFactory factory) {
		try {
			this.connection = factory.newConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCommunicationChannel() {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public void closeRabbitConnection() {
		try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
