package com.healthtraze.etraze.api.base.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.healthtraze.etraze.api.base.constant.Constants;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@CreatedDate
 	@Temporal(TemporalType.DATE)
	@Column(name = Constants.CREATEDON)
	protected Date createdOn;

 	@LastModifiedDate
  	@Temporal(TemporalType.DATE)
	@Column(name = Constants.MODIFIEDON)	
	protected Date modifiedOn;
	
 	@CreatedBy
	@Column(name = Constants.CREATEDBY)
	protected String createdBy;

	@Column(name = Constants.MODIFIEDBY)
	@LastModifiedBy
	protected String modifiedBy;

	@Column(name = Constants.VERSIONNO)
	@Version
	protected Integer versionNo;



	
	
	
	@Column(name = Constants.TENANTID)
	protected String tenantId;
	

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;

	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}



	
}
