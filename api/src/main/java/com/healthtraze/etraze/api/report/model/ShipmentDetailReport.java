package com.healthtraze.etraze.api.report.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="shipmantdetailreport")

public class ShipmentDetailReport {
	
	@Id
	private String inovicenumber;
	private String ticketid;
	private String manufacturer;
	private String manufacturerid;
	private String transportid;
	private String stockist;
	private String transporter;
	private String noofcases;
	private Integer daystaken;
	private String status;
	private String locality;
	private String tlt;
	@Temporal(TemporalType.DATE)
	private Date dispatcheddate;
	@Temporal(TemporalType.DATE)
	private Date deliverydate;
	private String location;
	private String customerrefnumber;
	private String ccdrequired;
	private String podrequired;
	private String tenantid;
	private String userid;
	
	
	public String getInovicenumber() {
		return inovicenumber;
	}
	public void setInovicenumber(String inovicenumber) {
		this.inovicenumber = inovicenumber;
	}
	public String getTicketid() {
		return ticketid;
	}
	public void setTicketid(String ticketid) {
		this.ticketid = ticketid;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getManufacturerid() {
		return manufacturerid;
	}
	public void setManufacturerid(String manufacturerid) {
		this.manufacturerid = manufacturerid;
	}
	public String getTransportid() {
		return transportid;
	}
	public void setTransportid(String transportid) {
		this.transportid = transportid;
	}
	public String getStockist() {
		return stockist;
	}
	public void setStockist(String stockist) {
		this.stockist = stockist;
	}
	public String getTransporter() {
		return transporter;
	}
	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}
	public String getNoofcases() {
		return noofcases;
	}
	public void setNoofcases(String noofcases) {
		this.noofcases = noofcases;
	}
	public Integer getDaystaken() {
		return daystaken;
	}
	public void setDaystaken(Integer daystaken) {
		this.daystaken = daystaken;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getTlt() {
		return tlt;
	}
	public void setTlt(String tlt) {
		this.tlt = tlt;
	}
	public Date getDispatcheddate() {
		return dispatcheddate;
	}
	public void setDispatcheddate(Date dispatcheddate) {
		this.dispatcheddate = dispatcheddate;
	}
	public Date getDeliverydate() {
		return deliverydate;
	}
	public void setDeliverydate(Date deliverydate) {
		this.deliverydate = deliverydate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCustomerrefnumber() {
		return customerrefnumber;
	}
	public void setCustomerrefnumber(String customerrefnumber) {
		this.customerrefnumber = customerrefnumber;
	}
	public String getCcdrequired() {
		return ccdrequired;
	}
	public void setCcdrequired(String ccdrequired) {
		this.ccdrequired = ccdrequired;
	}
	public String getPodrequired() {
		return podrequired;
	}
	public void setPodrequired(String podrequired) {
		this.podrequired = podrequired;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
}
