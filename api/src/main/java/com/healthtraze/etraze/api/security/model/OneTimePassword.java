package com.healthtraze.etraze.api.security.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.base.util.CommonUtil;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_one_time_password")
public class OneTimePassword extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -412481690804519495L;

	private static final int EXPIRATION = 5;

	@Id
	@Column(name = Constants.ID)
	private Long id;

	@Column(name =Constants.OTP)
	private String otp;

	@Column(name = Constants.USERID)
	private String userId;

	@Column(name = Constants.EMAILID)
	private String emailId;

	@Column(name = Constants.EXPIRYDATE)
	private LocalDateTime expiryDate;
	 
	@Column(name = Constants.OTPSESSIONID)
	private String otpSessionId;
	

	/**
	 * 
	 */
	public OneTimePassword() {
		super();
	}
	

	@Override
	public String toString() {
		return "OneTimePassword [id=" + id + ", otp=" + otp + ", userId=" + userId + ", emailId=" + emailId
				+ ", expiryDate=" + expiryDate + ", otpSessionId=" + otpSessionId + "]";
	}


	/**
	 * 
	 * 
	 * @param token
	 */
	public OneTimePassword(final String otp) {
		super();
		this.otp = otp;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	/**
	 * 
	 * 
	 * @param token
	 * @param user
	 */
	public OneTimePassword(final String otp, final String otpSessionId, final String userId, final String emailId) {
		super();
		this.otp = otp;
		this.userId = userId;
		this.emailId = emailId;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
		this.otpSessionId = otpSessionId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the otp
	 */
	public String getOtp() {
		return otp;
	}

	/**
	 * @param otp the otp to set
	 */
	public void setOtp(String otp) {
		this.otp = otp;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(final LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	private LocalDateTime calculateExpiryDate(final int expiryTimeInMinutes) {
          LocalDateTime  time= CommonUtil.getLocalDateTime();
		
		return time.plusMinutes(expiryTimeInMinutes);
		
	}

	public void updateToken(final String otp) {
		this.otp = otp;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	
	
	

	public String getOtpSessionId() {
		return otpSessionId;
	}

	public void setOtpSessionId(String otpSessionId) {
		this.otpSessionId = otpSessionId;
	}

	



	
	
}



