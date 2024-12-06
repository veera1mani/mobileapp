package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;



@Entity
@Table(name  ="tbl_screen")
public class Screen extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.SCREENID)
	protected String screenId;

	@Column(name = Constants.SCREENNAME)
	protected String screenName;

	@Column(name = Constants.SCREENDESCRIPTION)
	protected String screenDescription;

	@Column(name = Constants.ICON)
	protected String icon;

	@Column(name = Constants.PATH)
	protected String path;

	@Column(name =Constants.CREATEENABLE)
	protected boolean createEnable;

	@Column(name = Constants.UPDATEENABLE)
	protected boolean updateEnable;

	@Column(name = Constants.DELETEENABLE)
	protected boolean deleteEnable;

	@Column(name =Constants.VIEWENABLE)
	protected boolean viewEnable;

	@Column(name = Constants.SCREENORDER)
	protected Integer screenOrder;
	
	@Column(name =Constants.TYPE)
	protected String type;
	
	@Column(name =Constants.MENUID)
	protected String menuId;
	
	private String count;
	
	
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getScreenDescription() {
		return screenDescription;
	}

	public void setScreenDescription(String screenDescription) {
		this.screenDescription = screenDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCreateEnable() {
		return createEnable;
	}

	public void setCreateEnable(boolean createEnable) {
		this.createEnable = createEnable;
	}

	public boolean isUpdateEnable() {
		return updateEnable;
	}

	public void setUpdateEnable(boolean updateEnable) {
		this.updateEnable = updateEnable;
	}

	public boolean isDeleteEnable() {
		return deleteEnable;
	}

	public void setDeleteEnable(boolean deleteEnable) {
		this.deleteEnable = deleteEnable;
	}

	public boolean isViewEnable() {
		return viewEnable;
	}

	public void setViewEnable(boolean viewEnable) {
		this.viewEnable = viewEnable;
	}

	public Integer getScreenOrder() {
		return screenOrder;
	}

	public void setScreenOrder(Integer screenOrder) {
		this.screenOrder = screenOrder;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	

}
