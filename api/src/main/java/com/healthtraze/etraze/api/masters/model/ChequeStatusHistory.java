package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_cheque_status_history")
public class ChequeStatusHistory extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6934514461337865367L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "chequeNumber")
	private String chequeNumber;
	
	@Column(name = "chequeId")
	public Long chequeId;
	
	@Column(name = "sapId")
	private String sapId;
	
	@Column(name = "bankId")
	private String bankId;
	
	@Column(name = "bankName")
	private String bankName;
	
	@Column(name = "invoice")
	private String invoice;

	@Column(name = "status")
	private String status;

	@Column(name = "historyBy")
	private String historyBy;

	@Column(name = "historyOn")
	private Date historyOn;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "recivedVia")
	private String recivedVia;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public Long getChequeId() {
		return chequeId;
	}

	public void setChequeId(Long chequeId) {
		this.chequeId = chequeId;
	}

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHistoryBy() {
		return historyBy;
	}

	public void setHistoryBy(String historyBy) {
		this.historyBy = historyBy;
	}

	public Date getHistoryOn() {
		return historyOn;
	}

	public void setHistoryOn(Date historyOn) {
		this.historyOn = historyOn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRecivedVia() {
		return recivedVia;
	}

	public void setRecivedVia(String recivedVia) {
		this.recivedVia = recivedVia;
	}
	
	

}
