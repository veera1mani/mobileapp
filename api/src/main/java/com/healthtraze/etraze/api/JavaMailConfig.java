package com.healthtraze.etraze.api;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaMailConfig {
	
	private static Logger logger = LogManager.getLogger(JavaMailConfig.class);
	
	public static void main( String[] args ) throws MessagingException
	 {
	 String to = "priya.p@mavens-i.com";
	 String from = "no-reply@healthtraze.in";
	 final String username = "no-reply@healthtraze.in";//username generated by Pepipost
	 final String password = "zyvjijdksxwnriut";//password generated by Pepipost
	 String host = "smtp.gmail.com";
	 Properties props = new Properties();
	 props.put("mail.smtp.auth", "true");
	 props.put("mail.smtp.starttls.enable", "true");
	 props.put("mail.smtp.host", host);
	 props.put("mail.smtp.port", "587");
	
	 

	 Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	     @Override
	     protected PasswordAuthentication getPasswordAuthentication() {
	         return new PasswordAuthentication(username, password);
	     }
	 });
	
	 session.setDebug(true);
	 try {
	 // Create a default MimeMessage object.
	 Message message = new MimeMessage(session);
	 // Set From: header field
	 message.setFrom(new InternetAddress(from));
	 // Set To: header field
	 message.setRecipients(Message.RecipientType.TO,
	 InternetAddress.parse(to));
	 // Set Subject: header field
	 message.setSubject("My first message with JavaMail");
	 // Put the content of your message
	 message.setText("Hi there, this is my first message sent with JavaMail");
	 // Send message
	 logger.info("Sending msg");
	 Transport trans =session.getTransport("smtp");
	 trans.connect(host, 587, username, password);
	 trans.sendMessage(message, message.getAllRecipients());
	 trans.removeConnectionListener(null);
	 trans.close();
	 logger.info("Sent message successfully....");
	 } catch (MessagingException e) {
		    throw new MessagingException("An error occurred while processing a messaging exception", e);
	 }

	 }
}