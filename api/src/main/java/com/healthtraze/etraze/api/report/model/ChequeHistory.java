package com.healthtraze.etraze.api.report.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


public interface ChequeHistory {
	public String getStockist();
    public String getManufacture();
    public String getLocation();
    public String getBankname();
    public String getChequenumber();
    public String getStatus();
    public String getManufacturername();
    public String getSapid();
    public String getChequecategory();
    public Date getRecivedate();
    public String getRecivedvia();
    public Date getDepositdate();
    public String getInvoice();
    public BigDecimal getInvoiceamount();
    public String getAdjustedinvoices();
    public BigDecimal getAmount();
    public String getDtscndn();
    public BigDecimal getDtscndnamount();
    public String getRemarks();
    public String getLocality();
    public String getStockiststatus();
    public String getTennatid();
    public String getUserid();
	


}
