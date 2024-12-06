package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_ticket_status_history")
public class TicketStatusHistory extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3058746865337656774L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "ticketId")
	private String ticketId;

	@Column(name = "status")
	private String status;
	
	@Column(name = "invoice")
	private String invoice;

	@Column(name = "createdOn")
	private Date createdOn;
	
	@Column(name = "historyOn")
	private Date historyOn;

	@Column(name = "createdBy")
	private String createdBy;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="latitude")
	private String latitude;
	
	@Column(name="longitude")
	private String longitude;
	
	@Column(name="priority")
	private String priority;
	
	@Transient
	private int durationDate;
	

	public int getDurationDate() {
		return durationDate;
	}

	public void setDurationDate(int durationDate) {
		this.durationDate = durationDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getHistoryOn() {
		return historyOn;
	}

	public void setHistoryOn(Date historyOn) {
		this.historyOn = historyOn;
	}
	
	

}
