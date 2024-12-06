package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_bank")
public class Bank extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1905122041960251207L;

	@Override
	public String toString() {
		return "Bank [code=" + code + ", name=" + name + "]";
	}
	@Id
	@Column(name=Constants.CODE)
    private String code;
	
	@Column(name=Constants.NAME)
    private String name;
	
	@Column(name="status")
    private String status;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
 
    
    
}
