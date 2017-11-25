package org.feedhandler.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.feedhandler.feedhandler.FeedHandler;
import org.feedhandler.sender.FeedSender;

public class App {
	
	private static Properties properties = new Properties();
	private static InputStream propertiesFileInputStream = null;
	private static FeedSender feedSender;
	
	public static void main(String[] args) {
		setupProperties();
		setupRabbitMqCommunication();

		String subscriptionMessage = properties.getProperty("subscription.message");
		FeedHandler feedhandler = new FeedHandler(properties.getProperty("exchange.uri"));
		
		feedhandler.setMqFeedSender(feedSender);
		feedhandler.setSubscriptionMessage(subscriptionMessage);
		feedhandler.subscribeToSource();
	}
	
	private static void setupProperties() {
		loadPropertiesFile();
		loadPropertiesList();
	}
	
	private static void loadPropertiesFile() {
		try {
			propertiesFileInputStream = new FileInputStream("conf/app.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void setupRabbitMqCommunication() {
		HashMap<String, String> rabbitMqConnectionProperties = getRabbitMqConnectionProperties();

		feedSender = new FeedSender(rabbitMqConnectionProperties);
		feedSender.connect();
	}
	
	private static HashMap<String, String> getRabbitMqConnectionProperties() {
		HashMap<String, String> rabbitConnectionProperties = new HashMap<String, String>();
		
		rabbitConnectionProperties.put("host", properties.getProperty("rabbitmq.host"));
		rabbitConnectionProperties.put("channel", properties.getProperty("rabbitmq.channel"));
		rabbitConnectionProperties.put("username", properties.getProperty("rabbitmq.username"));
		rabbitConnectionProperties.put("password", properties.getProperty("rabbitmq.password"));
		
		return rabbitConnectionProperties;
	}
	
	private static void loadPropertiesList() {
		try {
			properties.load(propertiesFileInputStream);
			propertiesFileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
