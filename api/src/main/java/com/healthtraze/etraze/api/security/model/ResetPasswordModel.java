package com.healthtraze.etraze.api.security.model;

public class ResetPasswordModel {

	private String newPassword;
	private String token;

	
	
	
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
