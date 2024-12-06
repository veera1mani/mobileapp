package com.healthtraze.etraze.api.chat.model;

import java.util.List;

import com.healthtraze.etraze.api.base.model.BaseModel;

public class ChatResult extends BaseModel{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chat chat;
	private List<Chat> histroy;
	public Chat getChat() {
		return chat;
	}
	public void setChat(Chat chat) {
		this.chat = chat;
	}
	public List<Chat> getHistroy() {
		return histroy;
	}
	public void setHistroy(List<Chat> histroy) {
		this.histroy = histroy;
	}

}
