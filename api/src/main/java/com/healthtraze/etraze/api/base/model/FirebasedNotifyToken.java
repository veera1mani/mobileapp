package com.healthtraze.etraze.api.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;

import javax.persistence.Id;

@Entity
@Table(name = "tbl_notification_Token")
public class FirebasedNotifyToken extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.ID)
	private Long id;
	
	@Column(name = Constants.USERID)
	private String userId;
	
	@Column(name = Constants.TOKEN)
	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	

}
