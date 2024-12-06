package com.healthtraze.etraze.api.security.service;

import java.util.Base64;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.ChangePassword;
import com.healthtraze.etraze.api.security.model.LoginData;
import com.healthtraze.etraze.api.security.model.LoginResponse;
import com.healthtraze.etraze.api.security.model.ResetPasswordModel;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.repository.UserAuthRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;

@Service
public class SecurityService {

	private Logger logger = LogManager.getLogger(SecurityService.class);
	
	private final UserAuthRepository userAuthRepository;
	
	private final UserService userService;
	
	private final UserRepository userRepository;
	
	@Autowired(required = true)
	public SecurityService(UserAuthRepository userAuthRepository,UserService userService,UserRepository userRepository) {
		this.userAuthRepository = userAuthRepository;
		this.userService = userService;
		this.userRepository=userRepository;
	}
	
	
	/**
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public LoginResponse doLogin(LoginData data) {
		try {
			if (data != null) {
				Optional<UserAuth> a = userAuthRepository.findByUserId(data.getUserName());

				if (a.isPresent()) {
					UserAuth auth = a.get();
					String username = ("WEB".equals(data.getChennal())) ? auth.getEmailId() : null;
					
					return (auth.getPassword() != null && username != null
							&& username.equals(data.getUserName()) && auth.getPassword().equals(data.getPassword()))
									? userService.findUserById(auth.getUserId())
									: null;
				}
				
				
				
				
				
				
				
				
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param changePassword
	 * @return
	 */
	public boolean doPasswordChange(String channel, ChangePassword changePassword) {
		try {
			Optional<UserAuth> a = userAuthRepository.findByUserId(changePassword.getEmailId());
			
			if(a.isPresent()) {
			UserAuth auth = a.get();
						
			User u = userService.findById(auth.getUserId());
			if ("WEB".equals(channel)) {
				if (auth.getPassword().equals(changePassword.getOldPassword())) {
					
					Optional<UserAuth> users = userAuthRepository.findByUserId(changePassword.getEmailId());
					
					if(users.isPresent()) {
						UserAuth ua = users.get();
						ua.setPasscode(changePassword.getNewPassword());
						userAuthRepository.save(ua);
					}

					Optional<User> op  =  userRepository.findByUserId(channel);
					
					if(op.isPresent()) {
						User user = op.get();
						user.setNewUserValidateWeb(false);
						userRepository.save(user);
					}
					return true;
				}
			} else {
				if (auth.getPasscode().equals(changePassword.getOldPassword())) {
					auth.setPasscode(changePassword.getNewPassword());
					userAuthRepository.save(auth);
					u.setNewUserValidateMobile(false);
					userRepository.save(u);
					return true;
				}
			}

		} }catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return false;
	}

	
	
	
	
	
	
	/**
	 * 
	 * Verify user activation token before setting up the password from the user.
	 * 
	 * @param token
	 * @return
	 */
	public Result<?> verifyUserActivationToken(String token) {
		Result<?> result = new Result<>();
		try {
			 new String(Base64.getDecoder().decode(token.getBytes()));

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param passwordModel
	 * @return
	 */
	public Result<?> createNewPassword(ResetPasswordModel passwordModel) {
		Result<?> result = new Result<>();
		try {
		 new String(Base64.getDecoder().decode(passwordModel.getToken().getBytes()));
		
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	
	
	
	

	public boolean doResetPassword() {
	
		return false;
	}

	
	

}
