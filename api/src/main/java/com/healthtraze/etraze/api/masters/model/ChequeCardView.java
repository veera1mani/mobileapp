package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChequeCardView {
	
	@Id
	private String userid;
	private String tenantid;
	private long total;
	private long inhand;
	private long hold;
	private long security;
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getInhand() {
		return inhand;
	}
	public void setInhand(long inhand) {
		this.inhand = inhand;
	}
	public long getHold() {
		return hold;
	}
	public void setHold(long hold) {
		this.hold = hold;
	}
	public long getSecurity() {
		return security;
	}
	public void setSecurity(long security) {
		this.security = security;
	}
	
	
	
	

}
