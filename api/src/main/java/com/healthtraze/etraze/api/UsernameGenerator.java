package com.healthtraze.etraze.api;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class UsernameGenerator {
	private static Logger logger = LogManager.getLogger(UsernameGenerator.class);
	public static void main(String[] args) {
		String secondName;
		String firstName;
		String date;
		String username;
		try (Scanner in = new Scanner(System.in)) {
			try {
				logger.info("Please Enter Full Name: ");
				firstName = in.next();
				secondName = in.next();

				logger.info("Please DOB (DD/MM/YY): ");
				date = in.next();

				int num1 = (Integer.parseInt(date.substring(0, 2))+ Integer.parseInt(date.substring(3, 5)) / Integer.parseInt(date.substring(7)));
				// day + month / year in order to give unique digit

				int num2 = Integer.parseInt(date.substring(1, 2));
				// to ensure username is unique

				username = secondName.substring(0, (secondName.length() - 1)) + firstName.charAt(0) + num1 + num2;
				logger.info("Your Username Is: {}",username);
			} catch (Exception e) {
				logger.info("Input Error");
				e.printStackTrace();
			}
		}
	}
}
