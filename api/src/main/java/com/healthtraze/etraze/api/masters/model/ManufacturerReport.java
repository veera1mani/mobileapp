package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Data;



@Data
@Entity
@Table(name="tbl_manufacturer_report")
public class ManufacturerReport  extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name="manufacturerReportMappingId")
	private String manufacturerReportMappingId;
	
	@Column(name = "reportUrl")
	private String reportUrl;
	
	@Column(name ="manufacturerId")
	private String manufacturerId;
	
	
	@Column(name="reportFileName")
	private String reportFileName;
	@Column(name="month")
	private int month;
	
	@Column(name="year")
	private int year;


}
