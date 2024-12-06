package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;

import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Table(name  ="tbl_new_location_token")
public class NewLocationToken {
	@Id
	@Column(name = Constants.ID)
	private Long id;
	@Column(name = Constants.TOKEN)
	private String token;

 	@OneToOne
	private UserLocation userLocation;

	//

	public NewLocationToken() {
		super();
	}

	public NewLocationToken(final String token) {
		super();
		this.token = token;
	}

	public NewLocationToken(final String token, final UserLocation userLocation) {
		super();
		this.token = token;
		this.userLocation = userLocation;
	}

	//

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserLocation getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(UserLocation userLocation) {
		this.userLocation = userLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((getId() == null) ? 0 : getId().hashCode());
		result = (prime * result) + ((getToken() == null) ? 0 : getToken().hashCode());
		result = (prime * result) + ((getUserLocation() == null) ? 0 : getUserLocation().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NewLocationToken other = (NewLocationToken) obj;
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		if (getToken() == null) {
			if (other.getToken() != null) {
				return false;
			}
		} else if (!getToken().equals(other.getToken())) {
			return false;
		}
		if (getUserLocation() == null) {
			if (other.getUserLocation() != null) {
				return false;
			}
		} else if (!getUserLocation().equals(other.getUserLocation())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "NewLocationToken [id=" + id + ", token=" + token + ", userLocation=" + userLocation + "]";
	}

}
