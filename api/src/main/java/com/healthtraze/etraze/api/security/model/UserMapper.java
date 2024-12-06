package com.healthtraze.etraze.api.security.model;

import com.healthtraze.etraze.api.base.model.BaseModel;

public class UserMapper extends BaseModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private User user;
	private Role role;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
