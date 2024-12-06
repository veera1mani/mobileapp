package com.healthtraze.etraze.api.security.util;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.security.model.User;

public class MailUtil {

	
	public static String getUserActivationEmail(User user, String activationLink) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Dear" + user.getFirstName() + StringIteration.SPACE+ user.getLastName() + ",");
		stringBuilder.append(Constants.NEXTLINE);

		stringBuilder.append(
				"Welcome to Welldercare, your user account is ready to be activated, please click the link below to complete the activation process.");
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append(activationLink);
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append("Activation link will expire in 24 hours.");
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append("If you need assistance, please contact us at support@welldercare.life");
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append("This notification was sent by the Welldercare system, please do not respond to this email.");
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append("Thank you,");
		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append("The Welldercare Team");
		stringBuilder.append(Constants.NEXTLINE);

		stringBuilder.append(Constants.NEXTLINE);
		stringBuilder.append(
				"This e-mail is intended solely for the use of the individual or entity to which it is addressed. If you are not the intended recipient of this e-mail, or the employee or agent responsible for delivering this e-mail to the intended recipient, you are hereby notified that any dissemination, distribution, copying, or action taken in relation to the contents of and attachments to this e-mail is STRICTLY PROHIBITED and may be UNLAWFUL. If you have received");
		return stringBuilder.toString();

	}
	
	private MailUtil() {
		
	}
	
}
