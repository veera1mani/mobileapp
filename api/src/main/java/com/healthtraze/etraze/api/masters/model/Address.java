package com.healthtraze.etraze.api.masters.model;

import java.io.Serializable;

import lombok.ToString;

@ToString
public class Address implements Serializable {

	/**
	 * 
	 */
	public Address() {

	}

	private static final long serialVersionUID = 1L;
	private String address1;
	private String address2;
	private String state;
	private String city;
	private String country;
	private String pinCode;

	private String latitude;
	private String longitude;

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public Address(String address1, String address2, String state, String city, String country, String pinCode) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.state = state;
		this.city = city;
		this.country = country;
		this.pinCode = pinCode;

	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
