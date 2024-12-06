package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Embeddable
@Table(name  ="tbl_user_location")
public class UserLocation extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6401788873346061641L;
	@Id
	@Column(name =Constants.ID)
	private Long id;
	@Column(name =Constants.COUNTRY)
	private String country;
	@Column(name = Constants.ENABLED)
	private boolean enabled;

	@OneToOne
	private User user;

	public UserLocation() {
		super();
		enabled = false;
	}

	public UserLocation(String country, User user) {
		super();
		this.country = country;
		this.user = user;
		enabled = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
