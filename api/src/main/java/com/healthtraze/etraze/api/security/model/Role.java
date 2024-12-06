
package com.healthtraze.etraze.api.security.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_role")
public class Role extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.ROLEID)
	protected String roleId;

	@Column(name = Constants.ROLENAME)
	protected String roleName;

	@Column(name = Constants.DESCRIPTION)
	protected String description;

	@Transient
	private Set<RoleServices> roleServices;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RoleServices> getRoleServices() {
		return roleServices;
	}

	public void setRoleServices(Set<RoleServices> roleServices) {
		this.roleServices = roleServices;
	}

}
