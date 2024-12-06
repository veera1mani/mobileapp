package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Data;


@Data
@Entity
@Table(name="tbl_items")
public class Items extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6091338001879696533L;

	@Id
	private String id;
	
	@Column(name = "invoiceId")
	private String invoiceId;
	
	@Column(name = "productCode")
	private String productCode;	
	
	@Column(name = "productName")
	private String productName;
	
	@Column(name = "quantity")
	private String quantity;
	
	@Column(name = "batchNumber")
	private String batchNumber;
	
	@Column(name = "expiryDate")
	private String expiryDate;
	
	@Column(name = "mrp")
	private String mrp;
	
	@Column(name = "manufacturerId")
	private String manufacturerId;
	

}
