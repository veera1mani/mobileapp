package com.healthtraze.etraze.api.chat.model;

import com.healthtraze.etraze.api.base.model.BaseModel;


public class ChatContact extends BaseModel{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contactId;
	private String contactName;
	private long newMessageCount;
	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String string) {
		this.contactId = string;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public long getNewMessageCount() {
		return newMessageCount;
	}
	public void setNewMessageCount(long newMessageCount) {
		this.newMessageCount = newMessageCount;
	}
	
}
