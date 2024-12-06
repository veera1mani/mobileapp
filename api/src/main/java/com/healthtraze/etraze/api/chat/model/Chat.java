package com.healthtraze.etraze.api.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Table(name  = "tbl_chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name=Constants.CHATID)
	private long chatId;
	
	@Column(name=Constants.SENDER)
	private String sender;
	
	@Column(name=Constants.RECEIVER)
	private  String receiver;

	@Column(name=Constants.MESSAGE)
	private String message;

	@Column(name=Constants.SEEN)
	private Boolean seen;

	@Column(name=Constants.GROUPID)
	private Integer groupId;

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSeen() {
		return seen;
	}

	public void setSeen(Boolean seen) {
		this.seen = seen;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
