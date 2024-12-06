package com.healthtraze.etraze.api.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_user_auth")
public class UserAuth extends BaseModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "UserAuth [id=" + id + ", userId=" + userId + ", password=" + password + ", passcode=" + passcode
				+ ", emailId=" + emailId + ", authEnabled=" + authEnabled + ", passwordExpiryDate=" + passwordExpiryDate
				+ ", credentialsNonExpired=" + credentialsNonExpired + "]";
	}

	@Id
	@Column(name=Constants.ID)
	protected String id;
	
	@Column(name=Constants.USERID)
	protected String userId;
	
	@Column(name=Constants.PASSWORD)
	protected String password;
	
	@Column(name=Constants.PASSCODE)
	protected String passcode;
	
	@Column(name=Constants.EMAILID)
	protected String emailId;
	
	@Column(name=Constants.ENABLED)
	protected boolean authEnabled;
	
	@Column(name=Constants.PASSWORDEXPIRYDATE)
	protected Date passwordExpiryDate;
	
	@Column(name=Constants.CREDENTIALSEXPIRED) 
	protected boolean credentialsNonExpired;
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passcode
	 */
	public String getPasscode() {
		return passcode;
	}

	/**
	 * @param passcode the passcode to set
	 */
	public void setPasscode(String passcode) {
		this.passcode = passcode;
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

	/**
	 * @return the authEnabled
	 */
	public boolean isAuthEnabled() {
		return authEnabled;
	}

	/**
	 * @param authEnabled the authEnabled to set
	 */
	public void setAuthEnabled(boolean authEnabled) {
		this.authEnabled = authEnabled;
	}

	public Date getPasswordExpiryDate() {
		return passwordExpiryDate;
	}

	public void setPasswordExpiryDate(Date passwordExpiryDate) {
		this.passwordExpiryDate = passwordExpiryDate;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	

	
}
