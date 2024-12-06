package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Data;

@Entity
@Data
@Table(name="tbl_manufacturer_report_mapping")
public class ManufacturerReportMapping extends BaseModel{
	
	@Id
    @Column(name="manufacturerReportMappingId")
	private String manufacturerReportMappingId;
    
    @Column(name ="manufacturerId")
	private String manufacturerId;
    
    @Column(name="key")
	private String key;
    
	@Column(name="code")
	private String code;
	
	@Column(name ="reportName")
	private String reportName;
	
	@Column(name="value")
	private String value;
	
}
