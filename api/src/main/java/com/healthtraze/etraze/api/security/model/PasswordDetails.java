package com.healthtraze.etraze.api.security.model;

import java.io.Serializable;
import java.util.Date;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordDetails implements Serializable{

	/**
	 * 
	 */
	 private static final long serialVersionUID = 2405172041950251807L;

	private Date date;
	private String password;
	
	public PasswordDetails(Date date, String password) {
		super();
		this.date = date;
		this.password = password;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
