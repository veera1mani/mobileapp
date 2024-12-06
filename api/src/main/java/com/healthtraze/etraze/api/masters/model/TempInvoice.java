package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_temp_invoice")
public class TempInvoice extends BaseModel{
	
	private static final long serialVersionUID = -4663783900404907825L;

	@Id
	@Column(name = "id")
	private long id;
	
	@Column(name= "invoiceNumber")
	private String invoiceNumber;
	
	@Column(name= "ManufacturerId")
	private String ManufacturerId;
	
	@Column(name= "tenantId")
	private String tenantId;
	
	@Column(name= "ticketNumber")
	private String ticketNumber;
	
	@Column(name ="s3Url")
	private String s3Url;
	
	@Column(name="filePath")
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getManufacturerId() {
		return ManufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		ManufacturerId = manufacturerId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getS3Url() {
		return s3Url;
	}

	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}

	
}
