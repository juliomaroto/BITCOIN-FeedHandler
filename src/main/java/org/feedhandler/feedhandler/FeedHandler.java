package org.feedhandler.feedhandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.feedhandler.models.Transaction;
import org.feedhandler.sender.FeedSender;

public class FeedHandler {
	private final Integer TIME_WAITING_FOR_MESSAGES = 2000;
	private final String HEARTBEAT_MESSAGE = "pong";
	private final String HEARTBEAT_RESPONSE_KEYWORD = "hb";
	private final Integer SNAPSHOT_LENGTH_FLAG = 1000;

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
            		if (false == isHeartbeatMessage(message)) {            				
                		if (isValidMessage(message) && (false == isSnapshotMessage(message))) {
                			Transaction transaction = getTransaction(message);
            	    			feedSender.sendMessage(transaction.toString());

            	    			System.out.println(transaction.toString());
                		}
            		}
            }
        });
	}
	
	
	private Boolean isHeartbeatMessage(String message) {
		Boolean result = false;
		
		if (message.contains(HEARTBEAT_RESPONSE_KEYWORD))
			result = true;
			
		return result;
	}
	// Not subscription message
	private Boolean isValidMessage(String message) {
		boolean result = false;
		
		if (!message.contains("event"))
			result = true;
		
		return result;
	}
	
	// Check message length to detect if it is Delta message or a Exchange Snapshot
	private Boolean isSnapshotMessage(String message) {
		Boolean result = false;
		
		if (message.length() > SNAPSHOT_LENGTH_FLAG) {
			result = true;
		}
	
		return result;
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
	
	private Transaction getTransaction(String rawMessage) {
		rawMessage = rawMessage.replace("[", "");
		rawMessage = rawMessage.replace("]", "");
		
		StringTokenizer st = new StringTokenizer(rawMessage, ",");
		HashMap<String, String> message = new HashMap<String, String>();
		
		message.put("channelId", st.nextToken());
		st.nextToken();
		message.put("mts", st.nextToken());
		message.put("amount", st.nextToken());
		message.put("price", st.nextToken());
		
		Transaction transaction = new Transaction(message);
		
		return transaction;
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
