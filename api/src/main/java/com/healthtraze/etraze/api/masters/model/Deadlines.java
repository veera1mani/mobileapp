package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Data
@Entity
@Table(name = "tbl_deadlines")
public class Deadlines extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1018365516623830900L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "deadlineId")
	private Long deadlineId;
	
	@Column(name = "deadlineName")
	private String deadlineName;
	
	@Column(name = "deadlineType")
	private String deadlineType;
	
	@Column(name = "days")
	private int days;
	
	@Column(name = "userId")
	private String userId;

}
