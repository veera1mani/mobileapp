package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_manager_manufacturer_mapping")
public class ManagerManufacturerMapping {	
	
	@Id
	private String id; 
	private String manufacturerid;
	private String manufactureName;
	private String userId;
	private String tenantId;
	
}
