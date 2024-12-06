package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_return_note")
public class ReturnNote extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3434472537556674289L;

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "returnId")
	private String returnId;
	
	@Column(name = "serialNumber")
	private String serialNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "noteDate")
	private Date noteDate;

	@Column(name = "lineItem")
	private int lineItem;

	@Column(name = "noteType")
	private String noteType;

	@Column(name = "noteNumber")
	private String noteNumber;

	@Column(name = "claimType")
	private String claimType;
	
	@Transient
	private String remarks ;
	
	@Transient
	private String manufacturer ;
	
	@Transient
	private String grrnNumber ;

	@Transient
	private String invalidDate ;
	
	public String getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getGrrnNumber() {
		return grrnNumber;
	}

	public void setGrrnNumber(String grrnNumber) {
		this.grrnNumber = grrnNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}

	public int getLineItem() {
		return lineItem;
	}

	public void setLineItem(int lineItem) {
		this.lineItem = lineItem;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public String getNoteNumber() {
		return noteNumber;
	}

	public void setNoteNumber(String noteNumber) {
		this.noteNumber = noteNumber;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}


	
	
}
