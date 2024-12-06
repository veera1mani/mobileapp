package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_block_email")
public class BlockEmail extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1789616997986869064L;

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="blockValue")
	private String blockValue;
	
	@Column(name="blockType")
	private String blockType;
	
	@Column(name="tenantManufacturerId")
	private String tenantManufacturerId;
	
	@Column(name="manufacturerId")
	private String manufacturerId;
	

}
