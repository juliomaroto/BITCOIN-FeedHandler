package org.feedhandler.models;

import java.util.HashMap;

public class Transaction {
	
	private Integer channeldId;
	private Integer millisecondTimestamp;
	private Double amount;
	private Double price;
	
	public Transaction(HashMap<String, String> message) {
		this.channeldId = Integer.parseInt(message.get("channelId"));
		this.millisecondTimestamp = Integer.parseInt(message.get("mts"));
		this.amount = Double.parseDouble(message.get("mts"));
		this.price = Double.parseDouble(message.get("price"));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("CHANNEL_ID=");
		sb.append(channeldId);
		sb.append(", ");
		 
		sb.append("MTS=");
		sb.append(millisecondTimestamp);
		sb.append(", ");
		
		sb.append("AMOUNT=");
		sb.append(amount);
		sb.append(", ");
		
		sb.append("PRICE=");
		sb.append(price);
		
		return sb.toString();
	}
}
