
package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;

@Entity
//@Embeddable
@Table(name="tbl_role_services")
public class RoleServices {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = Constants.ID)
	protected Integer id;

	@Column(name = Constants.ROLEID)
	protected String roleId;

	@Column(name = Constants.SCREENID)
	protected String screenId;

	@Column(name = Constants.USERID)
	protected String userId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
