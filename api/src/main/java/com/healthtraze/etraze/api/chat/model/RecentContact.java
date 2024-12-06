package com.healthtraze.etraze.api.chat.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;




@Data

@NoArgsConstructor
public class RecentContact {

	private String contactId;
	private String contactName;
	private String contactImageUrl;
	private String latestMessage;
	private Date lastMessageDate;
	private boolean online;
	private int count;
	
	
	
	
	
	
	public RecentContact(String contactId, String contactName, String contactImageUrl, String latestMessage,
			Date date, boolean online, int count) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactImageUrl = contactImageUrl;
		this.latestMessage = latestMessage;
		this.lastMessageDate = date;
		this.online = online;
		this.count = count;
	}
	
	
	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactImageUrl() {
		return contactImageUrl;
	}
	public void setContactImageUrl(String contactImageUrl) {
		this.contactImageUrl = contactImageUrl;
	}
	public String getLatestMessage() {
		return latestMessage;
	}
	public void setLatestMessage(String latestMessage) {
		this.latestMessage = latestMessage;
	}
	public Date getLastMessageDate() {
		return lastMessageDate;
	}
	public void setLastMessageDate(Date lastMessageDate) {
		this.lastMessageDate = lastMessageDate;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	

}
