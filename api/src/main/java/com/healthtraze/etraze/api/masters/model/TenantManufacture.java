package com.healthtraze.etraze.api.masters.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_tenant_manufacture")
public class TenantManufacture {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "manufacturerId")
	private String manufacturerId;

	@Column(name = "tenantId")
	private String tenantId;

	@Column(name = "licenceNumber")
	private String licenceNumber;

	@Column(name = "emailId")
	private String emailId;
	
	@Column(name="password")
	private String password;
	
	@Column(name="host")
	private String host;
	
	@Temporal(TemporalType.DATE)
	@Column(name="emailDate")
	private Date emailDate;
	
	@Column(name="port")
	private long port;
	
	@Column(name = "status")
	private String status;
	
	@Column(name="stateId")
	private String stateId;

	@Column(name = "cityId")
	private String cityId;
	
	@Column(name="country")
	private String country;
	
	@Column(name = "pinCode")
	private String pinCode;
	
	@Column(name = "distributionModel")
	private String distributionModel;

	@Column(name = "location")
	private String location;
	
	@Column(name = "checkedNotGrrn")
	private int checkedNotGrrn;
	
	@Column(name = "grrnNotSecondCheque")
	private int grrnNotSecondCheque;
	
	@Column(name = "recivedNotChecked")
	private int recivedNotChecked;
	
	@Column(name = "orderNotFulfiled")
	private int orderNotFulfiled;
	
	@Column(name = "invoiceNotDispached")
	private int invoiceNotDispached;
	
	@Column(name = "deadlineClaim")
	private int deadlineClaim;
	
	@Column(name = "deadlineOrder")
	private int deadlineOrder;
	
	@Column(name = "deadlineTicket")
	private int deadlineTicket;
	
	@Column(name = "dispachedToDelivired")
	private int dispachedToDelivired;
	
	@Column(name = "notInvoiced")
	private int notInvoiced;
	
	@Column(name = "secondCheck")
	private boolean secondCheck;
	
	@Column(name = "isWmsed")
	private boolean isWmsed;
	
	@Column(name = "multiQr")
	private boolean multiQr;
	
	@Column(name = "floorWise")
	private boolean floorWise = true;
	
	public boolean isFloorWise() {
		return floorWise;
	}

	public void setFloorWise(boolean floorWise) {
		this.floorWise = floorWise;
	}

	public boolean isMultiQr() {
		return multiQr;
	}

	public void setMultiQr(boolean multiQr) {
		this.multiQr = multiQr;
	}

	public boolean isWmsed() {
		return isWmsed;
	}

	public void setWmsed(boolean isWmsed) {
		this.isWmsed = isWmsed;
	}

	@Transient
	private String manufacturerName;
	
	public List<String> getBlockEmails() {
		return blockEmails;
	}

	public void setBlockEmails(List<String> blockEmails) {
		this.blockEmails = blockEmails;
	}

	@Transient
	private List<String> blockEmails;
	
	@Transient
	private List<String> blockSubject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getDistributionModel() {
		return distributionModel;
	}

	public void setDistributionModel(String distributionModel) {
		this.distributionModel = distributionModel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCheckedNotGrrn() {
		return checkedNotGrrn;
	}

	public void setCheckedNotGrrn(int checkedNotGrrn) {
		this.checkedNotGrrn = checkedNotGrrn;
	}

	public int getGrrnNotSecondCheque() {
		return grrnNotSecondCheque;
	}

	public void setGrrnNotSecondCheque(int grrnNotSecondCheque) {
		this.grrnNotSecondCheque = grrnNotSecondCheque;
	}

	public int getRecivedNotChecked() {
		return recivedNotChecked;
	}

	public void setRecivedNotChecked(int recivedNotChecked) {
		this.recivedNotChecked = recivedNotChecked;
	}

	public int getOrderNotFulfiled() {
		return orderNotFulfiled;
	}

	public void setOrderNotFulfiled(int orderNotFulfiled) {
		this.orderNotFulfiled = orderNotFulfiled;
	}

	public int getInvoiceNotDispached() {
		return invoiceNotDispached;
	}

	public void setInvoiceNotDispached(int invoiceNotDispached) {
		this.invoiceNotDispached = invoiceNotDispached;
	}

	public int getDeadlineClaim() {
		return deadlineClaim;
	}

	public void setDeadlineClaim(int deadlineClaim) {
		this.deadlineClaim = deadlineClaim;
	}

	public int getDeadlineOrder() {
		return deadlineOrder;
	}

	public void setDeadlineOrder(int deadlineOrder) {
		this.deadlineOrder = deadlineOrder;
	}

	public int getDeadlineTicket() {
		return deadlineTicket;
	}

	public void setDeadlineTicket(int deadlineTicket) {
		this.deadlineTicket = deadlineTicket;
	}

	public int getDispachedToDelivired() {
		return dispachedToDelivired;
	}

	public void setDispachedToDelivired(int dispachedToDelivired) {
		this.dispachedToDelivired = dispachedToDelivired;
	}

	public int getNotInvoiced() {
		return notInvoiced;
	}

	public void setNotInvoiced(int notInvoiced) {
		this.notInvoiced = notInvoiced;
	}

	public boolean isSecondCheck() {
		return secondCheck;
	}

	public void setSecondCheck(boolean secondCheck) {
		this.secondCheck = secondCheck;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public List<String> getBlockSubject() {
		return blockSubject;
	}

	public void setBlockSubject(List<String> blockSubject) {
		this.blockSubject = blockSubject;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}


	
	
	
	

}
