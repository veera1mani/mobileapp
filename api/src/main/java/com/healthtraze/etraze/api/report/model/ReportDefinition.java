package com.healthtraze.etraze.api.report.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_report_definition")
public class ReportDefinition {

	@Id
	@Column(name="reportId")
	private String reportId;
	
	@Column(name="reportName")
	private String reportName;
	
	@Column(name="reportType")
	private String reportType;
	
	@Column(name="reportDesc")
	private String reportDesc;
	
	@Column(name="status")
	private String status;

	@Column(name = "column_wise_sum")
	private Boolean columnWiseSum;
	
	@Column(name = "column_wise_avg")
	private Boolean columnWiseAvg;
	
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "reportId")
	private Set<ReportColumnConfig> reportColumnConfigs;
	
	@Column(name="orderBy")
	private Integer orderBy;
	
	public Boolean getColumnWiseSum() {
		return columnWiseSum;
	}

	public void setColumnWiseSum(Boolean columnWiseSum) {
		this.columnWiseSum = columnWiseSum;
	}

	public Boolean getColumnWiseAvg() {
		return columnWiseAvg;
	}

	public void setColumnWiseAvg(Boolean columnWiseAvg) {
		this.columnWiseAvg = columnWiseAvg;
	}
	
	

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReportDesc() {
		return reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	public Set<ReportColumnConfig> getReportColumnConfigs() {
		return reportColumnConfigs;
	}

	public void setReportColumnConfigs(Set<ReportColumnConfig> reportColumnConfigs) {
		this.reportColumnConfigs = reportColumnConfigs;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	
	
	
	
}
