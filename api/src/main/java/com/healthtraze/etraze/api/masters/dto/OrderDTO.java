package com.healthtraze.etraze.api.masters.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;

public class OrderDTO {

	private String customerRefNumber;
	private String ticketId;
	private String tenantId; 
	private String podRequired;
	private String remarks;
	private String stockistId;
	private String transporter;
	private String vehicaleNo;
	private String status;
	private String invoice;
	private Ticket ticket;
	private String numOfCases;
	private String ccdRequired;
	private String lrNumber;
	private Date lrRecieveDate;
	private String lrDocument;
	private String longitude;
	private String latitude;
	public Boolean getPriority() {
		return priority;
	}
	public void setPriority(Boolean priority) {
		this.priority = priority;
	}
	private String historyId;
	private Boolean priority;
	private List<TicketOrderInvoice> invoices;
	
	private List<String> packIds;
	
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	private List<String> invoiceNumbers; 
	
	private String address;
	private Date deliveryDate;
	private String invoiceValue;
	private Date dueDate; 
	
	@Transient
	private String invalidDueDate;
	
	@Transient
	private String invalidDeliveryDate;
	
	@Transient
	private String invalidLrReceivedDate;
	
	public String getInvalidDueDate() {
		return invalidDueDate;
	}
	public void setInvalidDueDate(String invalidDueDate) {
		this.invalidDueDate = invalidDueDate;
	}
	public String getInvalidDeliveryDate() {
		return invalidDeliveryDate;
	}
	public void setInvalidDeliveryDate(String invalidDeliveryDate) {
		this.invalidDeliveryDate = invalidDeliveryDate;
	}
	public String getInvalidLrReceivedDate() {
		return invalidLrReceivedDate;
	}
	public void setInvalidLrReceivedDate(String invalidLrReceivedDate) {
		this.invalidLrReceivedDate = invalidLrReceivedDate;
	}
	public String getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	private String packId;
	
	public String getCustomerRefNumber() {
		return customerRefNumber;
	}
	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
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
	public String getStockistId() {
		return stockistId;
	}
	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}
	public List<TicketOrderInvoice> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<TicketOrderInvoice> invoices) {
		this.invoices = invoices;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public String getNumOfCases() {
		return numOfCases;
	}
	public void setNumOfCases(String numOfCases) {
		this.numOfCases = numOfCases;
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
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	public List<String> getPackIds() {
		return packIds;
	}
	public void setPackIds(List<String> packIds) {
		this.packIds = packIds;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPackId() {
		return packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}
	public List<String> getInvoiceNumbers() {
		return invoiceNumbers;
	}
	public void setInvoiceNumbers(List<String> invoiceNumbers) {
		this.invoiceNumbers = invoiceNumbers;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
