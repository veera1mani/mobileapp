package com.healthtraze.etraze.api.masters.dto;

import java.math.BigInteger;

import lombok.Data;
import lombok.ToString;

@Data
@ToString 
public class ChequeDTO {
	private BigInteger total;
	private BigInteger security;
	private BigInteger inward;
	private BigInteger hold;
	private BigInteger cancelled;
	private BigInteger outward;
	private BigInteger balance;
	private BigInteger inhand;
	private String threshold;
	private String stockistId;
	private String stockistName;
	private String sapId;
	private String manufacture;
	private String location;
	private String category;
	private String bankName;
	private String chequeNumber;
	private String chequeStatus;
	private String status;
	private String amount;
	private String invoice;
	private String invoiceAmount;
	private String dueDate;
	private String receiveDate;
	private String receiveVia;
	private String depositedate;
	private String deliveryDate;
	private String adjustInvoice;
	private String dtscndn;
	private String dtscndnAmount;
	private String remarks;
	private String local;
	private String code ;
	private String value;

	private String invoiceDate;
		private String tlt;
		private String dispatchedDate;
		private String daysTaken;
	 
	
	public BigInteger getTotal() {
		return total;
	}
	public void setTotal(BigInteger total) {
		this.total = total;
	}
	public BigInteger getSecurity() {
		return security;
	}
	public void setSecurity(BigInteger security) {
		this.security = security;
	}
	public BigInteger getInward() {
		return inward;
	}
	public void setInward(BigInteger inward) {
		this.inward = inward;
	}
	public BigInteger getOutward() {
		return outward;
	}
	public void setOutward(BigInteger outward) {
		this.outward = outward;
	}
	public BigInteger getBalance() {
		return balance;
	}
	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}
	public String getStockistId() {
		return stockistId;
	}
	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}
	public String getStockistName() {
		return stockistName;
	}
	public void setStockistName(String stockistName) {
		this.stockistName = stockistName;
	}
	public String getSapId() {
		return sapId;
	}
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public BigInteger getCancelled() {
		return cancelled;
	}
	public void setCancelled(BigInteger cancelled) {
		this.cancelled = cancelled;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigInteger getHold() {
		return hold;
	}
	public void setHold(BigInteger hold) {
		this.hold = hold;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getChequeStatus() {
		return chequeStatus;
	}
	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	


}
