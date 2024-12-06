package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table(name="tbl_auditHistory")
@Entity
public class AuditHistory extends BaseModel{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2015468070201006281L;

	@Id
	@Column(name="id")
	public String id;
	
	@Column(name="audit_id")
	public String audit_id;
   
	@Column(name="bfrvalue")
	public String bfrvalue;
	
     @Column(name="field")
     public String field;   	
     
	@Column(name="afrvalue")
	public String afrvalue;


	
	
	
	

}
