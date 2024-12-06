package com.healthtraze.etraze.api.masters.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name = "tbl_list_value")
public class ListValue extends BaseModel {
	private static final long serialVersionUID = 2405132041950251807L;

	@Id
	@Column(name = Constants.KEY)
	private String key;

	@Column(name = Constants.DESCRIPTION)
	private String description;

	@Override
	public String toString() {
		return "ListValue [key=" + key + ", description=" + description + ", values=" + values + "]";
	}

	@Column(name = Constants.VALUES)
	@ElementCollection
	private List<Domain> values;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Domain> getValues() {
		return values;
	}

	public void setValues(List<Domain> values) {
		this.values = values;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
