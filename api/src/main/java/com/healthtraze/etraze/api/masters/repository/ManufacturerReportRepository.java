package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ManufacturerReport;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.projections.ManuafctureTenantReport;
import com.healthtraze.etraze.api.masters.projections.ManufactureReport;

@Repository 
public interface ManufacturerReportRepository  extends BaseRepository<ManufacturerReport, String>  {
	
	
	@Query(value = "SELECT lv.name AS name ,mr.report_url AS reportUrl ,lv.value As value, mrm.manufacturer_report_mapping_id As manufacturerReportMappingId,mr.id AS id,mrm.report_name AS reportName,report_file_name AS fileName FROM tbl_manufacturer_report_mapping mrm left join tbl_manufacturer_report mr on mr.manufacturer_report_mapping_id = mrm.manufacturer_report_mapping_id AND mr.tenant_id =:tenantId  and mr.month=:month and mr.year=:year  left join list_value_values lv on lv.code = mrm.code AND lv.list_value_key = mrm.key  WHERE  mrm.manufacturer_id=:manufacturerId ",nativeQuery = true)
	public List<ManufactureReport> getAllManufacturerReport(String manufacturerId,String tenantId,int month,int year);
	
	@Query(value = "SELECT mrm.code,lv.value AS value , lv.name AS name ,COUNT(tm.manufacturer_id) AS total, COUNT(mr.report_url) AS uploaded,COUNT(CASE WHEN mr.report_url IS NULL THEN 1 END) AS notUploaded, "
            + "STRING_AGG(DISTINCT CASE WHEN mr.report_url IS NOT NULL THEN tm.tenant_id ELSE NULL END, ',') AS uploadedTenants, "
            + "STRING_AGG(DISTINCT CASE WHEN mr.report_url IS NULL THEN tm.tenant_id ELSE NULL END, ',') AS notUploadedTenants "
            + "FROM tbl_tenant_manufacture tm "
            + "LEFT JOIN tbl_manufacturer_report_mapping mrm "
            + "ON mrm.manufacturer_id = tm.manufacturer_id "
            + "AND mrm.manufacturer_id = :manufacturerId "
            + "LEFT JOIN tbl_manufacturer_report mr "
            + "ON mr.manufacturer_report_mapping_id = mrm.manufacturer_report_mapping_id "
            + "AND mr.manufacturer_id = mrm.manufacturer_id "
            + "AND mr.tenant_id = tm.tenant_id  and  mr.month=:month and mr.year=:year  "
            + "left join list_value_values lv on lv.code = mrm.code AND lv.list_value_key = mrm.key "
            + "WHERE tm.manufacturer_id = :manufacturerId and tm.tenant_id ILIKE %:tenantId%  "
            + "GROUP BY mrm.code,lv.value,lv.name  ", nativeQuery = true)
  public  List<ManufactureReport> getAllReportToManufacturer(String manufacturerId,String tenantId,int month,int year);
	
	
	@Query(value = "SELECT mrm.code,lv.value AS value , lv.name AS name , mr.report_url AS reportUrl "
            + "FROM tbl_tenant_manufacture tm "
            + "LEFT JOIN tbl_manufacturer_report_mapping mrm "
            + "ON mrm.manufacturer_id = tm.manufacturer_id "
            + "AND mrm.manufacturer_id = :manufacturerId "
            + "LEFT JOIN tbl_manufacturer_report mr "
            + "ON mr.manufacturer_report_mapping_id = mrm.manufacturer_report_mapping_id "
            + "AND mr.manufacturer_id = mrm.manufacturer_id "
            + "AND mr.tenant_id = tm.tenant_id and mr.month=:month and mr.year=:year "
            + "left join list_value_values lv on lv.code = mrm.code AND lv.list_value_key = mrm.key "
            + "WHERE tm.manufacturer_id = :manufacturerId and tm.tenant_id ILIKE %:tenantId%  "
            + "GROUP BY mrm.code,lv.value,lv.name ,mr.report_url ", nativeQuery = true)
  public  List<ManufactureReport> getAllReportToManufacturerURL(String manufacturerId,String tenantId,int month,int year);
	
	@Query(value = "select t.tenant_id As tenantId,t.tenant_name As tenanName,t.phone_number AS phoneNumber,mr.report_url AS ReportUrl from tbl_tenant t \r\n"
			+ "left join tbl_tenant_manufacture tm ON t.tenant_id = tm.tenant_id AND tm.manufacturer_id = :manufacturerId\r\n"
			+ "left join tbl_manufacturer_report mr ON mr.tenant_id = tm.tenant_id \r\n"
			+ "left join tbl_manufacturer_report_mapping mrp on mrp.manufacturer_report_mapping_id=mr.manufacturer_report_mapping_id and mrp.manufacturer_id=mr.manufacturer_id "
			+ "where mr.manufacturer_id = :manufacturerId and mrp.code=:code AND t.tenant_id IN (:tenantId)  group by t.tenant_id,t.phone_number,mr.report_url,t.tenant_name ",nativeQuery = true)
	public List<ManuafctureTenantReport> getAllTenantReport(String manufacturerId,List<String> tenantId,String code);

	
	@Query(value="select t.tenant_id As tenantId,t.tenant_name As tenanName,t.phone_number AS phoneNumber from tbl_tenant t where  t.tenant_id IN (:tenantId) ",nativeQuery = true)
	public List<ManuafctureTenantReport> getAllTenantReport(List<String> tenantId);
	
	@Query(value= "select t.tenant_id As tenantId,t.tenant_name As tenanName FROM tbl_tenant t left join tbl_tenant_manufacture on t.tenant_id = t.tenant_id where tm.manufacturer_id=:manufacturerId",nativeQuery = true)
	public List<ManuafctureTenantReport> findTenantByManufacture(String manufacturerId);
	
}
