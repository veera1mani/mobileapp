package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="tbl_user_role_services")
public class UserRoleService extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5277875056761936300L;

	@Id
	@Column(name = Constants.ID)
	protected String id;

	@Column(name = Constants.ROLEID)
	protected String roleId;

	@Column(name = Constants.SCREENID)
	protected String screenId;
	
	@Column(name = Constants.SCREENNAME)
	protected String screenName;

	@Column(name = Constants.USERID)
	protected String userId;
	

}
