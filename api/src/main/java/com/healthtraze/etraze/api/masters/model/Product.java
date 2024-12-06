package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ProductComposite.class)
@Table(name = "tbl_product")
public class Product {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 4246884049409597361L;

	@Id
	@Column(name = "productCode")
	private String productCode;

	@Column(name = "productName")
	private String productName;
	
	@Id
	@Column(name = "manufacturer")
	private String manufacturer;

	@Column(name = "wareHouseLocation")
	private String wareHouseLocation;

	@Column(name = "bin")
	private String bin;

	@Column(name = "row")
	private String row;

	@Column(name = "pallet")
	private String pallet;
	
	@Id
	@Column(name = Constants.TENANTID)
	private String tenantId;
	
	@CreatedDate
 	@Temporal(TemporalType.DATE)
	@Column(name = Constants.CREATEDON)
	protected Date createdOn;

 	@LastModifiedDate
  	@Temporal(TemporalType.DATE)
	@Column(name = Constants.MODIFIEDON)	
	protected Date modifiedOn;
	
 	@CreatedBy
	@Column(name = Constants.CREATEDBY)
	protected String createdBy;

	@Column(name = Constants.MODIFIEDBY)
	@LastModifiedBy
	protected String modifiedBy;

	@Column(name = Constants.VERSIONNO)
	@Version
	protected Integer versionNo;
	
	@Transient
	private String remarks;
	
	@Transient
	private String wareHouseLocationName;
	
	@Transient
	private String manufacturerName;

}
