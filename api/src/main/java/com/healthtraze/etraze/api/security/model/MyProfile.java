package com.healthtraze.etraze.api.security.model;

import java.util.List;


public class MyProfile {

	
	private User user;
	private Object userOnboardData;
	

	private List<User> veterans;
	
	
	public List<User> getVeterans() {
		return veterans;
	}
	public void setVeterans(List<User> veterans) {
		this.veterans = veterans;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Object getUserOnboardData() {
		return userOnboardData;
	}
	public void setUserOnboardData(Object userOnboardData) {
		this.userOnboardData = userOnboardData;
	}
	
	
}
