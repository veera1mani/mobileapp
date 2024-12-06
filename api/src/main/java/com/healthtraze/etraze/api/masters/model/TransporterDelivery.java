package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tbl_transporter_delivery")
public class TransporterDelivery extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 695173112017606563L;

	@Id
	@Column(name = "deliveryId")
	private String deliveryId;
	
	@Column(name = "manufacturerId")
	private String manufacturerId;
	
	@Column(name = "transporterId")
	private String transporterId;
	
 	@Temporal(TemporalType.DATE)
	@Column(name ="deliveryDate")
	protected Date deliveryDate;
	
	@Column(name = "transporterExpenses")
	private double transporterExpenses;
	
	@Transient
	private String remarks ;
	
	@Transient
	private String month;
	
	@Transient
	private String year;
	
	

}
