package com.healthtraze.etraze.api.masters.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@ToString
@Entity

@Table(name = "tbl_transport")

public class Transport extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "transportId")
	private String transportId;
	
	
	@Column(name = "status")
	private String status;

	@Column(name = "firstName", length = 100)
	private String firstName;
	@Column(name = "lastName", length = 100)
	private String lastName;

	@Column(name = "transportName")
	private String transportName;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;
	@Column(name = "pincode")
	private int pincode;
	@Column(name = "latitude", precision = 10, scale = 8)
	private BigDecimal latitude;
	@Column(name = "longitude", precision = 10, scale = 8)
	private BigDecimal longitude;
	@Column(name = "mobile", length = 10)
	private String mobile;
	@Column(name = "email")
	private String email;
	private String location;
	private String state;
	private String country;
	@Transient
	private String stateName;
	@Column(name = "isenable")
	private boolean isEnable;

}
