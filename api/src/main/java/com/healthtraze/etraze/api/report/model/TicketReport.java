package com.healthtraze.etraze.api.report.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ticketreport")
public class TicketReport {
    
    @Id
    private String ticketnumber;
    private String manufacturer;
    private String category;
    private Integer  deadline;
    private String manufacturerid ;
    
    public String getManufacturerid() {
		return manufacturerid;
	}
	public void setManufacturerid(String manufacturerid) {
		this.manufacturerid = manufacturerid;
	}
	public Integer getDeadline() {
		return deadline;
	}
	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}
	public Integer getCompletiondate() {
		return completiondate;
	}
	public void setCompletiondate(Integer completiondate) {
		this.completiondate = completiondate;
	}
 	@Temporal(TemporalType.DATE)
	private Date emaildate;
	
	private String stockist;
    private String assignto;
    private String status;
    private String location;
    private String remarks;
    private Integer days;
 
    private Integer completiondate;
    private String locality;
    private String tenantid;
    private String userid;
	public String getTicketnumber() {
		return ticketnumber;
	}
	public void setTicketnumber(String ticketnumber) {
		this.ticketnumber = ticketnumber;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String getStockist() {
		return stockist;
	}
	public Date getEmaildate() {
		return emaildate;
	}
	public void setEmaildate(Date emaildate) {
		this.emaildate = emaildate;
	}
	public void setStockist(String stockist) {
		this.stockist = stockist;
	}
	public String getAssignto() {
		return assignto;
	}
	public void setAssignto(String assignto) {
		this.assignto = assignto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

 
}
