package com.healthtraze.etraze.api.masters.documents;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.base.util.CommonUtil;


@Entity
@Table(name  ="tbl_country")

public class Country extends BaseModel{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name=Constants.ID)
	protected String id;
	
	@Column(name=Constants.COUNTRYCODE)
	protected String countryCode;
	
	@Column(name=Constants.COUNTRYNAME)
	protected String countryName;
	
   @Column(name="status")
   protected String status;

	public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName!=null?CommonUtil.firstLetterCapital(countryName):countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	
	
	
}
