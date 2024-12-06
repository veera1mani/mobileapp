package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tbl_user_manager")
public class UserManager extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2949049755688624128L;
	@Id
	private String id;
	private String manager;
	private String userId;

}
