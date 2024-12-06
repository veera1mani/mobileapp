package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_stockist_manufacture")
public class StockistManufacture {
	
	@Id
	@Column(name = "id")
	private long id;

	@Column(name="stockistId")
    private String stockistId;


	@Column(name="manufacture")
	private String manufacture;
	
	@Column(name="manufactureName")
	private String manufactureName;

	
	@Override
	public String toString() {
		return "StockistManufacture [id=" + id + ", stockistId=" + stockistId + ", manufacture=" + manufacture
				+ ", creditDays=" + creditDays + ", tlt=" + tlt + ", location=" + location + ", chequeCategory="
				+ chequeCategory + ", sapId=" + sapId + ", threshold=" + threshold + ", isEnable=" + isEnable + "]";
	}

	@Column(name="creditDays")
	private String creditDays;
	
	@Column(name="tlt")
	private String tlt;
	
	@Column(name="location")
	private String location;
	
	@Column(name="chequeCategory")
	private String chequeCategory;
	
	@Column(name="sapId")
	private String sapId;
	
	
	@Column(name="threshold")
	private String threshold;
	
	@Column(name="isenable")
	private boolean isEnable;
	
	@Column(name="tenantId")
	private String tenantId;
	


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(String creditDays) {
		this.creditDays = creditDays;
	}

	public String getTlt() {
		return tlt;
	}

	public void setTlt(String tlt) {
		this.tlt = tlt;
	}

	public String getChequeCategory() {
		return chequeCategory;
	}

	public void setChequeCategory(String chequeCategory) {
		this.chequeCategory = chequeCategory;
	}

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getStockistId() {
		return stockistId;
	}

	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}
	
	
	


	
	

}
