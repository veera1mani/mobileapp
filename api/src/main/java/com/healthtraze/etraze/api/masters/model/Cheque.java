package com.healthtraze.etraze.api.masters.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;  

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.ToString;

@Entity
@Table(name = "tbl_cheques")
@ToString
public class Cheque extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1484376450517388000L;

	@Id
	@Column(name = "chequeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long chequeId;

	@Column(name = "status")
	private String status;

	@Column(name = "stockistId")
	private String stockistId;

	@Column(name = "chequeNumber")
	private String chequeNumber;

	@Column(name = "invoice")
	private String invoice;

	@Column(name = "sapId")
	private String sapId;

	@Column(name = "reciveDate")
	@Temporal(TemporalType.DATE)
	private Date reciveDate;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "bankId")
	private String bankId;

	@Transient
	private String bankName;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "depositDate")
	@Temporal(TemporalType.DATE)
	private Date depositDate;

	@Column(name = "recivedVia")
	private String recivedVia;

	@Column(name = "chequeCancelDate")
	private Date chequeCancelDate;

	@Column(name = "dtscndn")
	private String dtsCnDn;

	@Column(name = "dtscndnamount")
	private BigDecimal dtsCnDnAmount;

	@Column(name = "adjustedinvoices")
	private String adjustedInvoices;

	@Column(name = "ischequehold")
	private Boolean isChequeHold;
	
	@Column(name = "isreturned")
	private Boolean isReturned;

	@Column(name = "invoicehold")
	private String invoiceHold;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "invoiceAmount")
	private BigDecimal invoiceAmount;
	
	@Transient
	private String invalid;
	
	@Transient
	private String invalidReceivedDate;
	
	@Transient
	private String invalidReceivedVia;
	
	@Transient
	private String invaliddepositDate;
	
	@Transient
	private String invalidchequeNumber;

	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	public Long getChequeId() {
		return chequeId;
	}

	public void setChequeId(Long chequeId) {
		this.chequeId = chequeId;
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

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public Date getReciveDate() {
		return reciveDate;
	}

	public void setReciveDate(Date reciveDate) {
		this.reciveDate = reciveDate;
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
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public String getRecivedVia() {
		return recivedVia;
	}

	public void setRecivedVia(String recivedVia) {
		this.recivedVia = recivedVia;
	}

	public Date getChequeCancelDate() {
		return chequeCancelDate;
	}

	public void setChequeCancelDate(Date chequeCancelDate) {
		this.chequeCancelDate = chequeCancelDate;
	}

	public String getDtsCnDn() {
		return dtsCnDn;
	}

	public void setDtsCnDn(String dtsCnDn) {
		this.dtsCnDn = dtsCnDn;
	}

	public BigDecimal getDtsCnDnAmount() {
		return dtsCnDnAmount;
	}

	public void setDtsCnDnAmount(BigDecimal dtsCnDnAmount) {
		this.dtsCnDnAmount = dtsCnDnAmount;
	}

	public String getAdjustedInvoices() {
		return adjustedInvoices;
	}

	public void setAdjustedInvoices(String adjustedInvoices) {
		this.adjustedInvoices = adjustedInvoices;
	}

	public Boolean getIsChequeHold() {
		return isChequeHold;
	}

	public void setIsChequeHold(Boolean isChequeHold) {
		this.isChequeHold = isChequeHold;
	}

	public String getInvoiceHold() {
		return invoiceHold;
	}

	public void setInvoiceHold(String invoiceHold) {
		this.invoiceHold = invoiceHold;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Boolean getIsReturned() {
		return isReturned;
	}

	public void setIsReturned(Boolean isReturned) {
		this.isReturned = isReturned;
	}

	public String getInvalidReceivedDate() {
		return invalidReceivedDate;
	}

	public void setInvalidReceivedDate(String invalidReceivedDate) {
		this.invalidReceivedDate = invalidReceivedDate;
	}

	public String getInvaliddepositDate() {
		return invaliddepositDate;
	}

	public void setInvaliddepositDate(String invaliddepositDate) {
		this.invaliddepositDate = invaliddepositDate;
	}

	public String getInvalidchequeNumber() {
		return invalidchequeNumber;
	}

	public void setInvalidchequeNumber(String invalidchequeNumber) {
		this.invalidchequeNumber = invalidchequeNumber;
	}

	public String getInvalidReceivedVia() {
		return invalidReceivedVia;
	}

	public void setInvalidReceivedVia(String invalidReceivedVia) {
		this.invalidReceivedVia = invalidReceivedVia;
	}


}
