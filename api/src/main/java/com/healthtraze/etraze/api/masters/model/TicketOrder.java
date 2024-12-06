package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_ticket_order")
public class TicketOrder extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7405553050161548649L;

	@Id
	@Column(name = "ticketId")
	private String ticketId;

	@Column(name = "customerRefNumber")
	private String customerRefNumber;

	@Column(name = "podRequired")
	private String podRequired;
	
	@Column(name = "ccdRequired")
	private String ccdRequired;

	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "transporter")
	private String transporter;
	
	@Column(name = "vehicaleNo")
	private String vehicaleNo;
	
	@Column(name = "numOfCases")
	private String numOfCases;
	
	@Column(name = "lrNumber")
	private String lrNumber;
	
	@Column(name = "lrRecieveDate")
	private Date lrRecieveDate;
	
	@Column(name = "lrDocument")
	private String lrDocument;
	
	@Column(name = "isCreated")
	private boolean isCreated;
	
	private String TransportarName;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public String getPodRequired() {
		return podRequired;
	}

	public void setPodRequired(String podRequired) {
		this.podRequired = podRequired;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getVehicaleNo() {
		return vehicaleNo;
	}

	public void setVehicaleNo(String vehicaleNo) {
		this.vehicaleNo = vehicaleNo;
	}

	

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public String getNumOfCases() {
		return numOfCases;
	}

	public void setNumOfCases(String numOfCases) {
		this.numOfCases = numOfCases;
	}

	public String getTransportarName() {
		return TransportarName;
	}

	public void setTransportarName(String transportarName) {
		TransportarName = transportarName;
	}

	public String getCcdRequired() {
		return ccdRequired;
	}

	public void setCcdRequired(String ccdRequired) {
		this.ccdRequired = ccdRequired;
	}

	public String getLrNumber() {
		return lrNumber;
	}

	public void setLrNumber(String lrNumber) {
		this.lrNumber = lrNumber;
	}

	public Date getLrRecieveDate() {
		return lrRecieveDate;
	}

	public void setLrRecieveDate(Date lrRecieveDate) {
		this.lrRecieveDate = lrRecieveDate;
	}

	public String getLrDocument() {
		return lrDocument;
	}

	public void setLrDocument(String lrDocument) {
		this.lrDocument = lrDocument;
	}
	
	
	
	
	

	
	
	
	
}
