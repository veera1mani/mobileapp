package com.healthtraze.etraze.api.masters.documents;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_date_format")
public class DateFormat extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String dateFormatId;
	private String dateOrder;
	private String dateFormat;
	
	
	
	public String getDateFormatId() {
		return dateFormatId;
	}
	public void setDateFormatId(String dateFormatId) {
		this.dateFormatId = dateFormatId;
	}
	public String getDateOrder() {
		return dateOrder;
	}
	public void setDateOrder(String dateOrder) {
		this.dateOrder = dateOrder;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
