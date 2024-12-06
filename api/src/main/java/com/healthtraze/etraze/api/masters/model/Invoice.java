package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tbl_invoice")
public class Invoice extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1955507182231671138L;

	@Id	
	@Column(name = "invoiceNumber")
	private String invoiceNumber;
	
	@Column(name = "invoiceDate")
	private Date invoiceDate;
	
	
	@Column(name = "dueDate")
	private Date dueDate;
	
	
	@Column(name = "invoiceValue")
	private String invoiceValue;
	
	@Column(name = "manufacturerId")
	private String manufacturerId;
	
	@Transient
	private Items[] items;
	
	@Transient
	private String remarks;
	
	
	
	
}
