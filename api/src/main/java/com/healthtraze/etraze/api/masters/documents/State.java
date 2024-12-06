package com.healthtraze.etraze.api.masters.documents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.base.util.CommonUtil;

import javax.persistence.Id;


@Entity
@Table(name  ="tbl_state")
public class State extends BaseModel{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name =Constants.ID)
	protected String id;
	
	@Column(name =Constants.COUNTRYCODE)
	protected String countryCode;
	
	@Column(name=Constants.STATECODE)
	protected String stateCode;
	
	@Column(name=Constants.STATENAME)
	protected String stateName;
	@Column(name="status")
	protected String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName!=null?CommonUtil.firstLetterCapital(stateName):stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
		
	

}
