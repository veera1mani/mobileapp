package com.healthtraze.etraze.api.masters.documents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.base.util.CommonUtil;

import javax.persistence.Id;


@Entity
@Table(name  ="tbl_city")
public class City extends BaseModel{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name=Constants.ID)
	protected String id;
	
	@Column(name = Constants.COUNTRYCODE)
	protected String countryCode;
	
	@Column(name=Constants.STATECODE)
	protected String stateCode;
	
	@Column(name=Constants.CITYCODE)
	protected String cityCode;
	
	@Column(name=Constants.CITYNAME)
	protected String cityName;
	
	@Column(name="status")
	protected String status;
	
	@Transient
	private String stateName;
	
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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName!=null?CommonUtil.firstLetterCapital(cityName):cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	

	


}
