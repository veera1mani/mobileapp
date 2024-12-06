package com.healthtraze.etraze.api.security.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_verification_token")
public class VerificationToken {

	private static final int EXPIRATION = 60 * 24;

	@Id
	@Column(name = Constants.ID)
	private Long id;

	@Column(name = Constants.TOKEN)
	private String token;

	@Column(name = Constants.USER)
	private UserAuth user;

	@Column(name = Constants.EXPIRYDATE)
	private LocalDateTime expiryDate;
	
	@Column(name = Constants.STATUS)
	private String status;


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * 
	 */
	public VerificationToken() {
		super();
	}

	
	/**
	 * 
	 * 
	 * @param token
	 */
	public VerificationToken(final String token) {
		super();
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param token
	 * @param user
	 */
	public VerificationToken(final String token, final UserAuth user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public UserAuth getUser() {
		return user;
	}

	public void setUser(final UserAuth user) {
		this.user = user;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(final LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	private LocalDateTime calculateExpiryDate(final int expiryTimeInMinutes) {
		LocalDateTime dateTime =LocalDateTime.now(); 
		return	dateTime.plusDays(expiryTimeInMinutes);
	}

	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getExpiryDate() == null) ? 0 : getExpiryDate().hashCode());
		result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
		result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final VerificationToken other = (VerificationToken) obj;
		if (getExpiryDate() == null) {
			if (other.getExpiryDate() != null) {
				return false;
			}
		} else if (!getExpiryDate().equals(other.getExpiryDate())) {
			return false;
		}
		if (getToken() == null) {
			if (other.getToken() != null) {
				return false;
			}
		} else if (!getToken().equals(other.getToken())) {
			return false;
		}
		if (getUser() == null) {
			if (other.getUser() != null) {
				return false;
			}
		} else if (!getUser().equals(other.getUser())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Token [String=").append(token).append("]").append("[Expires").append(expiryDate).append("]");
		return builder.toString();
	}

}
