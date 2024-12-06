package com.healthtraze.etraze.api.masters.projections;

public interface ManufactureReport {
	
	public String getCode();
	public String getValue();
	public String getName();
	public String getTotal();
	public String getUploaded();
	public String getNotUploaded();
	public String getUploadedTenants();
	public String getNotUploadedTenants();
	public String getReportUrl();
	public String getId();
	public String getManufacturerReportMappingId();
	public String getReportName();
	public String getFileName();

}
