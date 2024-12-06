package com.healthtraze.etraze.api.masters.model;
 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;
 
@Entity
@Table(name = "tbl_ticket")
public class Ticket extends BaseModel {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6331029798163731495L;

	@Id
	@Column(name = "ticketId")
	private String ticketId;
 
	@Column(name = "subject")
	private String subject;
 
	@Column(name = "status")
	private String status;
 
	@Column(name = "type")
	private String type;
 
	@Column(name = "emailedBy")
	private String emailedBy;
 
	@Column(name = "assignedTo")
	private String assignedTo;
 
	@Column(name = "assignedOn")
	private String assignedOn;
 
	@Column(name = "manufacturerId")
	private String manufacturerId;
	
	
	@Column(name = "stockistId")
	private String stockistId;
	
	@Column(name = "sentDate")
	private Date sentDate;	

	@Column(name = "createdDate")
	private Date createdDate;	

	@Transient
	private String role;
	
	@Transient
	private String remarks;
	
	@Transient
	private TicketOrder order;
	
	@Transient
	private String userName;
	
	@Transient
	private String stockistName;
	
	@Transient
	private String location;
	
	@Transient
	private String durationDate;
	
	@Column(name= "manufacturer")
	private String manufacturer;
	
	@Column(name= "eml")
	private String eml;
	
	@Column(name= "filePath")
	private String filePath;
 
	public String getTicketId() {
		return ticketId;
	}
 
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
 
	public String getSubject() {
		return subject;
	}
 
	public void setSubject(String subject) {
		this.subject = subject;
	}
 
	public String getStatus() {
		return status;
	}
 
	public void setStatus(String status) {
		this.status = status;
	}
 
	public String getType() {
		return type;
	}
 
	public void setType(String type) {
		this.type = type;
	}
 
	public String getAssignedTo() {
		return assignedTo;
	}
 
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
 
	public String getAssignedOn() {
		return assignedOn;
	}
 
	public void setAssignedOn(String assignedOn) {
		this.assignedOn = assignedOn;
	}
 
	public String getManufacturerId() {
		return manufacturerId;
	}
 
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
 
	public String getEmailedBy() {
		return emailedBy;
	}
 
	public void setEmailedBy(String emailedBy) {
		this.emailedBy = emailedBy;
	}
 
	public String getRole() {
		return role;
	}
 
	public void setRole(String role) {
		this.role = role;
	}
 
	public String getDurationDate() {
		return durationDate;
	}
 
	public void setDurationDate(String durationDate) {
		this.durationDate = durationDate;
	}
 
	public String getManufacturer() {
		return manufacturer;
	}
 
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
 
	public String getStockistId() {
		return stockistId;
	}
 
	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}
 
	public String getUserName() {
		return userName;
	}
 
	public void setUserName(String userName) {
		this.userName = userName;
	}
 
	public String getStockistName() {
		return stockistName;
	}
 
	public void setStockistName(String stockistName) {
		this.stockistName = stockistName;
	}
 
	public String getEml() {
		return eml;
	}
 
	public void setEml(String eml) {
		this.eml = eml;
	}
 
	public String getLocation() {
		return location;
	}
 
	public void setLocation(String location) {
		this.location = location;
	}
 
	public TicketOrder getOrder() {
		return order;
	}
 
	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
}