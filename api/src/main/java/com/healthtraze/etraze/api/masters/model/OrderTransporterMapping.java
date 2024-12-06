package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_order_transporter_mapping")
public class OrderTransporterMapping extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 124394326531021877L;
	@Id
	private String id;
	private String ticketId;
	private String transporterId;
	private String vehicleNo;
}
