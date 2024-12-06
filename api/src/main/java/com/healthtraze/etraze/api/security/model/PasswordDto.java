package com.healthtraze.etraze.api.security.model;

import com.healthtraze.etraze.api.security.validation.ValidPassword;

public class PasswordDto {



	private String token;

	@ValidPassword
	private String newPassword;
	
	private String oldPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

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
