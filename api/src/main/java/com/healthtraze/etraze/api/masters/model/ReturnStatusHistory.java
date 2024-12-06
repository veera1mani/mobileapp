package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_return_status_history")
public class ReturnStatusHistory extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4509002881738046723L;

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "returnId")
	private String returnId;

	@Column(name = "serialNumber")
	private String serialNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "historyBy")
	private String historyBy;

	@Column(name = "historyOn")
	private Date historyOn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHistoryBy() {
		return historyBy;
	}

	public void setHistoryBy(String historyBy) {
		this.historyBy = historyBy;
	}

	public Date getHistoryOn() {
		return historyOn;
	}

	public void setHistoryOn(Date historyOn) {
		this.historyOn = historyOn;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

}
