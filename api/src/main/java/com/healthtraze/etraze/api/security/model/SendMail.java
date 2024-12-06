package com.healthtraze.etraze.api.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.ToString;

@ToString
@Entity
@Table(name = "tbl_send_mail")
public class SendMail extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 330972958504703986L;

	@Id
	@Column(name = StringIteration.ID)
	private String id;

	@Column(name = Constants.TOEMAIL)
	private String[] to;

	@Column(name = Constants.FROMEMAIL)
	private String from;

	@Column(name = Constants.MESSAGE,columnDefinition = "text")
	private String message;

	@Column(name = Constants.PATH)
	private String path;

	@Column(name = Constants.CCEMAIL)
	private String[] cc;

	@Column(name = Constants.BCCEMAIL)
	private String[] bcc;

	@Column(name = Constants.CONTENTTYPE)
	private String contentType;

	@Column(name = Constants.STATUS)
	private String status;

	@Column(name = Constants.SUBJECT)
	private String subject;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = Constants.DELIVERDATE)
	private Date deliverDate;

	@Column(name = Constants.TONAME)
	private String toName;

	@Column(name = StringIteration.CREATEDON)
	private Date createdOn;

	@Column(name = Constants.RETRYCOUNT)
	private int retryCount;

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	
	

}
