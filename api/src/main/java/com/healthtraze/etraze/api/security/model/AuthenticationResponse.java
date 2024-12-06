package com.healthtraze.etraze.api.security.model;

public class AuthenticationResponse {
	private String token;

	public AuthenticationResponse(String jwt) {
		this.token = jwt;
	}

	public String getToken() {
		return token;
	}
	
	
	
	
	
}
