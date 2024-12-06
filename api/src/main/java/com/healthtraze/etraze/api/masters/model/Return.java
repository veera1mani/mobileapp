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

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.ToString;


@Entity
@ToString
@Table(name = "tbl_returns")
public class Return extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6072113051015515034L;

	@Id
	@Column(name = "returnId")
	private String returnId;
	
	@Column(name = "serialNumber",unique = true)
	private String serialNumber;
	
	@Column(name = "returnNumber")
	private String returnNumber;
	
	@Column(name = "stockistId")
	private String stockistId;
	
	@Column(name = "transporterId")
	private String transporterId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "salabletatus")
	private String salabletatus;
	
	@Column(name = "nonSalabletatus")
	private String nonSalabletatus;
	
	
	@Column(name = "claimNumber")
	private String claimNumber;
	
	@Column(name = "claimId")
	private String claimId;
	
	
  	@Temporal(TemporalType.DATE)
	@Column(name = "receivedDate")
	private Date receivedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "claimDate")	
	private Date claimDate;
	
	@Column(name = "lrNumber")
	private String lrNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "lrBookingDate")
	private Date lrBookingDate;
	
	@Column(name = "numberOfBoxes")
	private int numberOfBoxes;
	
	@Column(name = "numberOfLineItems")
	private int numberOfLineItems;
	
	@Column(name = "claimType")
	private String claimType;
	
	@Transient
	private String location;
	
	@Column(name="manufacturer")
	private String manufacturer;
	
	@Transient
	private boolean isSecondCheck;
	
	@Transient
	private String stockistName;
	
	@Transient
	private String ManufacturertName;
	
	@Transient
	private String grrnNumber;
	
	@Transient
	private List<ReturnNote> returnNotes;
	
	@Transient
	private String transporterName;

	@Column(name="document")
	private String document;
	
	@Column(name="docname")
	private String documentName;
	
	@Column(name="documentURL")
	private String documentURL;
	
	@Column(name="mobileDocumentName")
	private String mobileDocumentName;
	
	@Column(name="numOfNonSalableCases")
	private String numOfNonSalableCases;
	
	@Column(name="misMatch")
	private boolean misMatch;
	
	@Column(name="misMatchType")
	private String misMatchType;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="channel")
	private String channel;
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStockistId() {
		return stockistId;
	}

	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}

	public String getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(String transporterId) {
		this.transporterId = transporterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getLrNumber() {
		return lrNumber;
	}

	public void setLrNumber(String lrNumber) {
		this.lrNumber = lrNumber;
	}

	public Date getLrBookingDate() {
		return lrBookingDate;
	}

	public void setLrBookingDate(Date lrBookingDate) {
		this.lrBookingDate = lrBookingDate;
	}

	public int getNumberOfBoxes() {
		return numberOfBoxes;
	}

	public void setNumberOfBoxes(int numberOfBoxes) {
		this.numberOfBoxes = numberOfBoxes;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public int getNumberOfLineItems() {
		return numberOfLineItems;
	}

	public void setNumberOfLineItems(int numberOfLineItems) {
		this.numberOfLineItems = numberOfLineItems;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public boolean isSecondCheck() {
		return isSecondCheck;
	}

	public void setSecondCheck(boolean isSecondCheck) {
		this.isSecondCheck = isSecondCheck;
	}

	public String getStockistName() {
		return stockistName;
	}

	public void setStockistName(String stockistName) {
		this.stockistName = stockistName;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSalabletatus() {
		return salabletatus;
	}

	public void setSalabletatus(String salabletatus) {
		this.salabletatus = salabletatus;
	}

	public String getNonSalabletatus() {
		return nonSalabletatus;
	}

	public void setNonSalabletatus(String nonSalabletatus) {
		this.nonSalabletatus = nonSalabletatus;
	}

	public List<ReturnNote> getReturnNotes() {
		return returnNotes;
	}

	public void setReturnNotes(List<ReturnNote> returnNotes) {
		this.returnNotes = returnNotes;
	}

	public String getDocumentURL() {
		return documentURL;
	}

	public void setDocumentURL(String documentURL) {
		this.documentURL = documentURL;
	}

	public String getNumOfNonSalableCases() {
		return numOfNonSalableCases;
	}

	public void setNumOfNonSalableCases(String numOfNonSalableCases) {
		this.numOfNonSalableCases = numOfNonSalableCases;
	}

	public boolean isMisMatch() {
		return misMatch;
	}

	public void setMisMatch(boolean misMatch) {
		this.misMatch = misMatch;
	}

	public String getMisMatchType() {
		return misMatchType;
	}

	public void setMisMatchType(String misMatchType) {
		this.misMatchType = misMatchType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getManufacturertName() {
		return ManufacturertName;
	}

	public void setManufacturertName(String manufacturertName) {
		ManufacturertName = manufacturertName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(String returnNumber) {
		this.returnNumber = returnNumber;
	}

	public Date getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}

	public String getGrrnNumber() {
		return grrnNumber;
	}

	public void setGrrnNumber(String grrnNumber) {
		this.grrnNumber = grrnNumber;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getMobileDocumentName() {
		return mobileDocumentName;
	}

	public void setMobileDocumentName(String mobileDocumentName) {
		this.mobileDocumentName = mobileDocumentName;
	}

	
	
	
	
	

	
	
	
	
	
	
	


}
