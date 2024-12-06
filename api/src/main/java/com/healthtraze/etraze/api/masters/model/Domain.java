package com.healthtraze.etraze.api.masters.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.healthtraze.etraze.api.base.constant.Constants;

@Embeddable
public class Domain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1905122041960251207L;

	@Column(name = Constants.CODE)
	private String code;

	@Override
	public String toString() {
		return "Domain [code=" + code + ", name=" + name + ", value=" + value + "]";
	}

	@Column(name = Constants.NAME)
	private String name;
	
	@Column(name = Constants.VALUE)
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
