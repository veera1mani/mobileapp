package com.healthtraze.etraze.api.masters.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_tenant")
public class Tenant {
	
	
	@Id
	@Column(name = "tenantId")
	private String tenantId;
	
	@Column(name = "tenant_name")
	private String tenantName;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "logo")
	private String logo;
	
	
	@Column(name = "isSecondCheck")
	private boolean isSecondCheck;
	

	@Column(name = "isWmsed")
	private boolean isWmsed;
	
	@Column(name="tenantCode")
	private String tenantCode;

	@Transient
	private List<TenantManufacture> manufacture;
	

	public List<TenantManufacture> getManufacture() {
		return manufacture;
	}

	public void setManufacture(List<TenantManufacture> manufacture) {
		this.manufacture = manufacture;
	}

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

	public boolean isSecondCheck() {
		return isSecondCheck;
	}

	public void setIsSecondCheck(boolean isSecondCheck) {
		this.isSecondCheck = isSecondCheck;
	}

	public boolean isWmsed() {
		return isWmsed;
	}

	public void setWmsed(boolean isWmsed) {
		this.isWmsed = isWmsed;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public void setSecondCheck(boolean isSecondCheck) {
		this.isSecondCheck = isSecondCheck;
	}
	
}
