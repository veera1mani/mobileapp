package com.healthtraze.etraze.api.masters.documents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_manufacturer")
public class Manufacturer extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "manufacturerId")
	private String manufacturerId;

	@Column(name = "manufacturerName")
	private String manufacturerName;

	@Column(name="shortName",unique = true)
	private String shortName;
       
	@Column(name="status")
	private String status;
	
	@Column(name="email")
	private String email;
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="headQuarts")
      private String headQuarts;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeadQuarts() {
		return headQuarts;
	}

	public void setHeadQuarts(String headQuarts) {
		this.headQuarts = headQuarts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	@Override
	public String toString() {
		return "Manufacturer [manufacturerId=" + manufacturerId + ", manufacturerName=" + manufacturerName
				+ ", shortName=" + shortName + ", status=" + status + "]";
	}
	
	
}
