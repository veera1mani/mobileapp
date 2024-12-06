package com.healthtraze.etraze.api.masters.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.masters.dto.ItemsDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tbl_invoice_line_items")
public class InvoiceLineItems extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8321880158913189995L;

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
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "isChecked")
	private boolean isChecked;
	
	@Column(name = "isPicked")
	private boolean isPicked;
	
	@Column(name = "pickItems")
	private String pickItems;
	
	@Column(name = "partialPick")
	private boolean partialPick;
	
	@Column(name = "partialCheck")
	private boolean partialCheck;
	
	@Transient
	private List<ItemsDto> items;
	
	@Transient
	private String groupId;
	
}
