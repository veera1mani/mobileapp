package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_audit")
@Entity
public class Audit extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8271453047509022798L;

	@Id
	@Column(name = "id")
	public String id;

	@Column(name = "Transport_id")
	public String Transport_id;

}
