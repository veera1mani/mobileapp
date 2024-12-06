package com.healthtraze.etraze.api.masters.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_ticket_order_invoice")
public class TicketOrderInvoice  extends BaseModel{


	/**
	 * 
	 */
	private static final long serialVersionUID = 637270437817549257L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "ticketId")
	private String ticketId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "invoiceValue")
	private String invoiceValue;
	
	@Column(name = "dueDate")
	private Date dueDate;

	@Column(name = "invoiceNumber")
	private String invoiceNumber;

	@Column(name = "lineItem")
	private String lineItem;
	
	@Column(name = "numOfCases")
	private String numOfCases;

	@Column(name = "isSelected")
	private Boolean isSelected;
	
	@Column(name="selectList")
	private String selectList;
	
	@Column(name = "priority")
	private Boolean priority;
	
	@Column(name = "transporter")
	private String transporter;
	
	@Column(name = "vehicaleNo")
	private String vehicaleNo;
	
	@Column(name = "lrNumber")
	private String lrNumber;
	
	@Column(name = "lrRecieveDate")
	private Date lrRecieveDate;
	
	@Column(name = "lrDocument")
	private String lrDocument;
	
	@Column(name = "packId")
	private String packId;
	
	@Column(name = "chequeStatus")
	private Boolean chequeStatus;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "assignTransport")
	private Date assignTransport;
	
	@Column(name="deliveryDate")
	private Date deliveryDate;
	
	@Transient
	private List<InvoiceLineItems> items;

	
	
	public List<InvoiceLineItems> getItems() {
		return items;
	}

	public void setItems(List<InvoiceLineItems> items) {
		this.items = items;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}
 
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getAssignTransport() {
		return assignTransport;
	}

	


	public void setAssignTransport(Date assignTransport) {
		this.assignTransport = assignTransport;
	}

	@Transient
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Transient
	private String stockistName;
	
	@Transient
	private String Location;
	
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getNumOfCases() {
		return numOfCases;
	}

	public void setNumOfCases(String numOfCases) {
		this.numOfCases = numOfCases;
	}

	public String getSelectList() {
		return selectList;
	}

	public void setSelectList(String selectList) {
		this.selectList = selectList;
	}

	public Boolean getPriority() {
		return priority;
	}

	public void setPriority(Boolean priority) {
		this.priority = priority;
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

	public String getStockistName() {
		return stockistName;
	}

	public void setStockistName(String stockistName) {
		this.stockistName = stockistName;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public Boolean getChequeStatus() {
		return chequeStatus;
	}

	public void setChequeStatus(Boolean chequeStatus) {
		this.chequeStatus = chequeStatus;
	}
	
	
	
	

	

}
