package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.TenantManufacture;

public class TenantDTO {
	
	private String tenantId;
	private String tenantName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNumber;
	private String status;
	private String logo;
	private List<TenantManufacture> manufacture;
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public List<TenantManufacture> getManufacture() {
		return manufacture;
	}
	public void setManufacture(List<TenantManufacture> manufacture) {
		this.manufacture = manufacture;
	}
	
	

}
