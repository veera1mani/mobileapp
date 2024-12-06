package com.healthtraze.etraze.api.security.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration; 

public class SecurityUtil {

	private SecurityUtil() {
		
	}
	/**
	 * 
	 * Auto generated password
	 * 
	 */
	public static String getAutoPassword() {
		String capitalLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallLetter = "abcdefghijklmnopqrstuvwxyz";
		String number = Constants.SECURITYUTILNUMBER;
		String specialChar = "!@#$%^&*_=+-/.?<>)";

		String concWord = capitalLetter + smallLetter + number + specialChar;

		 SecureRandom randPass = new SecureRandom();
		int len = 10;
		char[] password = new char[len];

		for (int i = 0; i < len; i++) {
			password[i] = concWord.charAt(randPass.nextInt(concWord.length()));
		}
		return new String(Base64.getEncoder().encode(new String(password).getBytes()));
	}

	public static String getRandomString() {

		String capitalLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallLetter = "abcdefghijklmnopqrstuvwxyz";
		String number = Constants.SECURITYUTILNUMBER;

		String concWord = capitalLetter + smallLetter + number;

		SecureRandom randPass = new SecureRandom();
		int len = 10;
		char[] password = new char[len];

		for (int i = 0; i < len; i++) {
			password[i] = concWord.charAt(randPass.nextInt(concWord.length()));
		}
		return new String(password);
	}

	public static String getResetPasswordToken() {
		return SecurityUtil.getRandomString();
	}

	/**
	 * 
	 * Auto generated password
	 * 
	 */
	public static String getAutoPassCode() {
		String number =Constants.SECURITYUTILNUMBER;
		SecureRandom randPass = new SecureRandom();
		int len = 4;
		char[] password = new char[len];
		for (int i = 0; i < len; i++) {
			password[i] = number.charAt(randPass.nextInt(number.length()));
		}
		return new  String(password);
	}

	public static String getUserName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
			 
		}
	}
	

	
	
	
	

	
	public static String getRefferalCode() {

		
		String number = StringIteration.NUMBER;
		String concword =  number;
		SecureRandom randPass = new SecureRandom();
		int len = 6;
		char[] password = new char[len];
		for (int i = 0; i < len; i++) {
			password[i] = concword.charAt(randPass.nextInt(concword.length()));
		}
		return new String(password);
	}
	

	
}
