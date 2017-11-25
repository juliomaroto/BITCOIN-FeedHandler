package org.feedhandler.feedhandler;

import java.net.URI;
import java.net.URISyntaxException;

import org.feedhandler.sender.FeedSender;

public class FeedHandler {
	private final Integer TIME_WAITING_FOR_MESSAGES = 2000;
	private final String HEARTBEAT_MESSAGE = "pong";
	private final String HEARTBEAT_KEYWORD_RESPONSE = "hb";

	private String apiResourceAddress;
	private WebsocketClientEndpoint clientEndPoint;
	private FeedSender feedSender;
	private String subscriptionMessage;
	
	public FeedHandler(String apiResourceAddress) {
		this.apiResourceAddress = apiResourceAddress;
	}
	
	public void setSubscriptionMessage(String subscriptionMessage) {
		this.subscriptionMessage = subscriptionMessage;
	}
	
	public void setMqFeedSender(FeedSender feedSender) {
		this.feedSender = feedSender;
	}
	
	public void subscribeToSource() {
            openWebSocket();
            attachOnMessageEventListener();
            
            setUpCommunication();
            keepAliveConnection();
	}
	
	private void openWebSocket() {
		try {
			clientEndPoint = new WebsocketClientEndpoint(new URI(apiResourceAddress));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void attachOnMessageEventListener() {
		clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
            		if (!message.contains(HEARTBEAT_KEYWORD_RESPONSE)) {
                		System.out.println(message);
            		}
            		 
            		message = getMessageFormated(message);
            	    feedSender.sendMessage(message);
            }
        });
	}
	
	private void setUpCommunication() {
		sendSubscriptionMessage();
		waitForSetUpCommunicationMessages();
	}
	
	private void sendSubscriptionMessage() {
		clientEndPoint.sendMessage(subscriptionMessage);
	}
	
	private void waitForSetUpCommunicationMessages() {
		try {
			Thread.sleep(TIME_WAITING_FOR_MESSAGES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getMessageFormated(String message) {
		message = message.replace("[", "");
		message = message.replace("]", "");
		
		return message;
	}
	
	private void keepAliveConnection() {
		final Thread keepAliveThread = new Thread() {
		    public void run() {
		        setHeartbeat();
		    }
		    
		    private void setHeartbeat() {
			    	while (true) 
			    		clientEndPoint.sendMessage(HEARTBEAT_MESSAGE);
		    }
		};

		keepAliveThread.start();
	}
	
}
