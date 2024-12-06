package com.healthtraze.etraze.api.report.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_report_column_config")
public class ReportColumnConfig {
	@Id
	@Column(name = "columnId")
	private String columnId;

	@Column(name = "columnName")
	private String columnName;

	@Column(name = "columnKey")
	private String columnKey;

	@Column(name = "columnType")
	private String columnType;

	@Column(name = "width")
	private Integer width;

	@Column(name = "reportId")
	private String reportId;
	
	@Column(name = "orderBy")
	private Integer orderBy;
	
	@Column(name = "sum_required")
	private Boolean isSumRequired;
	
	
	@Column(name = "avg_required")
	private Boolean isAvgRequired;
	
	@Column(name = "count_required")
	private Boolean isCountRequired;
	
	@Column(name = "groupBy")
	private Boolean groupBy;
	
	@Column(name="groupBasedSum")
	private Boolean groupBasedSum;
	
	@Transient
	private String reportName;

	
	



	public Boolean getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}

	public Boolean getGroupBasedSum() {
		return groupBasedSum;
	}

	public void setGroupBasedSum(Boolean groupBasedSum) {
		this.groupBasedSum = groupBasedSum;
	}

	public Boolean getIsSumRequired() {
		return isSumRequired;
	}

	public void setIsSumRequired(Boolean isSumRequired) {
		this.isSumRequired = isSumRequired;
	}
	
	

	public Boolean getIsCountRequired() {
		return isCountRequired;
	}

	public void setIsCountRequired(Boolean isCountRequired) {
		this.isCountRequired = isCountRequired;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Boolean getIsAvgRequired() {
		return isAvgRequired;
	}

	public void setIsAvgRequired(Boolean isAvgRequired) {
		this.isAvgRequired = isAvgRequired;
	}

}
