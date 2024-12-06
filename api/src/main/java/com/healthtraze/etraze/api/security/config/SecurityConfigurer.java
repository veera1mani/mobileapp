package com.healthtraze.etraze.api.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.healthtraze.etraze.api.security.filter.JwtRequestFilter;
import com.healthtraze.etraze.api.security.service.UserService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	
	private final UserService myUserDetailsService;

	private final JwtRequestFilter jwtRequestFilter;
	
    @Autowired
	public SecurityConfigurer(UserService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
		this.myUserDetailsService = myUserDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				.antMatchers("/assets/**").permitAll()
				.antMatchers("/*").permitAll()
				
				.antMatchers("/login").permitAll()
				.antMatchers("/rest/api/v1/pin-login").permitAll()
			
				.antMatchers("/register").permitAll()
				.antMatchers("/generateOtp").permitAll()
				.antMatchers("/validateOtp").permitAll()
				.antMatchers("/resetPassword").permitAll()
				.antMatchers("/savePassword").permitAll()
				.antMatchers("/validate_reset_password_token").permitAll()
				.antMatchers("/userConfirm").permitAll()
				.antMatchers("/rest/api/v1/generalDocument/{id}").permitAll()
				.antMatchers("/rest/api/v1/ticket").permitAll()
				.antMatchers("/rest/api/v1/tenant-email").permitAll()
				.antMatchers("/socket/**").permitAll()
				.antMatchers("/rest/api/v1/downloadFilefromAWS/**").permitAll()

				//dummy
				.antMatchers("/validate_user_actiation_token/{token}").permitAll()
				.antMatchers("/recent-user-activation-link/{emailId}").permitAll()
				.antMatchers("/recent-recent-password-link/{emailId}").permitAll()
				.antMatchers("/recent-user-otp/{emailId}").permitAll()
				
				
				.antMatchers("/resendRegistrationToken").permitAll()
				.antMatchers("/rest/api/v1/emailTemplate").permitAll()
				.antMatchers("/validateToken/{token}").permitAll()
				.antMatchers("/validatePhoneNo/{phoneNo}").permitAll()
				.antMatchers("/validateUserId/{userId}").permitAll()
				.antMatchers("/check-user-exist/{email}").permitAll()
				.antMatchers("/check-mobile-pin-status/{email}").permitAll()
				.antMatchers("/set-mobile-pin").permitAll()
				.antMatchers("/rest/api/v1/downloadFile/{fileName:.+}").permitAll()
				.antMatchers("/rest/api/v1/download/{fileId}").permitAll()
				.antMatchers("/rest/api/v1/configdata").permitAll()
				.antMatchers("/rest/api/v1/post-enquiry").permitAll()
				.antMatchers("/rest/api/v1/countries").permitAll()
				.antMatchers("/rest/api/v1/state/country/{countryCode}").permitAll()
				.antMatchers("/rest/api/v1/city/state/{stateCode}").permitAll()
				.antMatchers("/rest/api/v1/territories/{cityCode}").permitAll()
				.antMatchers("/rest/api/v1/generalDocument/{id}").permitAll()
				.antMatchers("/rest/api/v1/emailVerification").permitAll()
				.antMatchers("/rest/api/v1/phoneNumberVerification").permitAll()
				.antMatchers("/rest/api/v1/event-notification").permitAll()
		        .antMatchers("/rest/api/v1/countries").permitAll()
		        .antMatchers("/rest/api/v1/ticket").permitAll()
				.antMatchers("/rest/api/v1/tenant-email").permitAll()
				.antMatchers("/rest/api/v1/tenants").permitAll()
				.antMatchers("/rest/api/v1/invoice-details").permitAll()
		        
		        
		    
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
