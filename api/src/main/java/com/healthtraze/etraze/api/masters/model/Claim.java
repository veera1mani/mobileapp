package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Data
@Getter
@Setter
@Table(name="tbl_claims")
public class Claim extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 377658952509353961L;

	@Id
	@Column(name = "claimId")
	private String claimId;
	
	@Column(name="claimNumber")
	private String claimNumber;
	
	@Column(name="stockistId")
	private String stockistId;
	
	@Column(name="status")
	private String status;
	
	@Column(name="document")
	private String document;
	
	@Column(name="docname")
	private String documentName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "receivedDate")	
	private Date receivedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "claimDate")	
	private Date claimDate;
	
	@Column(name="manufacturerId")
	private String manufacturerId;
	
	@Column(name="manufacturer")
	private String manufacturer;

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getStockistId() {
		return stockistId;
	}

	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	

	
	
	
	
	
	

	
	
	
	
	
	

	

}
