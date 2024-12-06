package com.healthtraze.etraze.api.security.model;

public class LoginData {

	private String userName;
	private String password;
	private String chennal;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChennal() {
		return chennal;
	}

	public void setChennal(String chennal) {
		this.chennal = chennal;
	}

}
