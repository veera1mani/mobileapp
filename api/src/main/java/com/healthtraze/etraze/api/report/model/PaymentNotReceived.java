package com.healthtraze.etraze.api.report.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="paymentnotreceived")
public class PaymentNotReceived {

		@Id
	private String invoicenumber;
		@Temporal(TemporalType.DATE)
	private Date invoicedate;
	private String stockist;
	private String manufacturename;
	private String manufacturerid;
	@Temporal(TemporalType.DATE)
	private Date deliverydate;
	@Temporal(TemporalType.DATE)
	private Date duedate;
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getStockist() {
		return stockist;
	}
	public void setStockist(String stockist) {
		this.stockist = stockist;
	}
	public String getManufacturename() {
		return manufacturename;
	}
	public void setManufacturename(String manufacturename) {
		this.manufacturename = manufacturename;
	}
	public String getManufacturerid() {
		return manufacturerid;
	}
	public void setManufacturerid(String manufacturerid) {
		this.manufacturerid = manufacturerid;
	}
	public Date getDeliverydate() {
		return deliverydate;
	}
	public void setDeliverydate(Date deliverydate) {
		this.deliverydate = deliverydate;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getDaystaken() {
		return daystaken;
	}
	public void setDaystaken(Integer daystaken) {
		this.daystaken = daystaken;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	private String remarks;
	private Integer daystaken;
	private String locality;
	private String location;
	private String tenantid;
	private String user_id;
	
	
}
