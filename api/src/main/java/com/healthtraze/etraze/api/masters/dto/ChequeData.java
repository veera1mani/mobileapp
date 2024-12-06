package com.healthtraze.etraze.api.masters.dto;

import java.util.Date;

import lombok.ToString;

@ToString
public class ChequeData {

	private String chequeNumber;
	private String status;
	private String stockistId;
	private String manufactureId;
	private int quantity;
	private String bankId;
	private String sapId;
	private Date reciveDate;
	private String recivedVia;
	
	
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStockistId() {
		return stockistId;
	}
	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}
	public String getManufactureId() {
		return manufactureId;
	}
	public void setManufactureId(String manufactureId) {
		this.manufactureId = manufactureId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public Date getReciveDate() {
		return reciveDate;
	}
	public void setReciveDate(Date reciveDate) {
		this.reciveDate = reciveDate;
	}
	public String getRecivedVia() {
		return recivedVia;
	}
	public void setRecivedVia(String recivedVia) {
		this.recivedVia = recivedVia;
	}
	public String getSapId() {
		return sapId;
	}
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
	
	
	
}
