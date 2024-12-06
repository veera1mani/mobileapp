package com.healthtraze.etraze.api.report.model;

public interface ColumnConfigDetails {
	String getReportName();
	
	String getColumnName();
	String getColumnKey();
	String getColumnType();
	Boolean getIsCountRequired();
	Boolean getIsSumRequired();
	Boolean getIsAvgRequired();
	String getReportId();
	Integer getWidth();
	Boolean getColumnWiseSum();
	Boolean getColumnWiseAvg();
	Boolean getGroupBy();
	Boolean getGroupBasedSum();
	String getReportDesc();
}
