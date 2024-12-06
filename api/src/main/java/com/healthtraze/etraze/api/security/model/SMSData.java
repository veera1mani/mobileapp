package com.healthtraze.etraze.api.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;

import javax.persistence.Id;




@Entity
@Table(name  ="tbl_send_sms")
public class SMSData   {
	


	@Id
	private long id;
	
	@Column(name=Constants.SMSID)
	private String smsId;
	
	@Column(name=Constants.TONAME)
	private String toName;
	
	@Column(name=Constants.MESSAGE)
	private String message;
	
	@Column(name=Constants.STATUS)
	private String status;
	
	@Column(name=Constants.DELIVERDATE)
	private Date deliverDate;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

  public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	
	@Override
	public String toString() {
		return "SmsData [id=" + id + ", SmsId=" + smsId + ", toName=" + toName + ", message=" + message + ", status="
				+ status + ", deliverDate=" + deliverDate + ", getId()=" + getId() + ", getSmsId()=" + getSmsId()
				+ ", getToName()=" + getToName() + ", getMessage()=" + getMessage() + ", getStatus()=" + getStatus()
				+ ", getDeliverDate()=" + getDeliverDate() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	

}
