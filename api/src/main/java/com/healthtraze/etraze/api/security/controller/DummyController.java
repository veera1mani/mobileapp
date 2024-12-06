/**
 * 
 */
package com.healthtraze.etraze.api.security.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.security.model.OneTimePassword;
import com.healthtraze.etraze.api.security.model.PasswordResetToken;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.model.VerificationToken;
import com.healthtraze.etraze.api.security.repository.OneTimePasswordRepository;
import com.healthtraze.etraze.api.security.repository.PasswordResetTokenRepository;
import com.healthtraze.etraze.api.security.repository.VerificationTokenRepository;

/**
 * @author mavensi
 *
 */

@CrossOrigin
@RestController
public class DummyController {
 

	
	VerificationTokenRepository repository;
	
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	OneTimePasswordRepository oneTimePasswordRepository;
	
	@Autowired
	public DummyController(VerificationTokenRepository repository,PasswordResetTokenRepository passwordResetTokenRepository,OneTimePasswordRepository oneTimePasswordRepository) {
		this.repository=repository;
		this.passwordResetTokenRepository=passwordResetTokenRepository;
		this.oneTimePasswordRepository=oneTimePasswordRepository;
	}
	private Logger logger = LogManager.getLogger(DummyController.class);

	@GetMapping(value = "/recent-user-activation-link/{emailId}")
	public String recentUserActivationLink(@PathVariable String emailId) {

		VerificationToken token = new VerificationToken();
		UserAuth user = new UserAuth();

		user.setEmailId(emailId);

		token.setUser(user);
		Optional<VerificationToken> ot = repository.findOne(Example.of(token));
		if (ot.isPresent()) {
			String t = ot.get().getToken();

			logger.error(t);

			return t;
		}

		return null;
	}

	@SuppressWarnings("unused")
	@GetMapping(value = "/recent-recent-password-link/{emailId}")
	public String recentPasswordRestLink(@PathVariable String emailId) {

		PasswordResetToken p = new PasswordResetToken();

		UserAuth u = new UserAuth();
		u.setEmailId(emailId);
		u.setAuthEnabled(true);

		p.setUser(u);

		PasswordResetToken op = null;

		if (op != null) {
			return op.getToken();
		}

		return null;
	}


	@GetMapping(value = "/recent-user-otp/{emailId}")
	public String recentOtp(@PathVariable String emailId) {

		OneTimePassword oneTimePassword = new OneTimePassword();

		oneTimePassword.setEmailId(emailId);

		Optional<OneTimePassword> o = oneTimePasswordRepository.findOne(Example.of(oneTimePassword));
		if (o.isPresent()) {
			return o.get().getOtp();
		}
		return "000000";
	}



}
