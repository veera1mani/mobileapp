package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.dto.TransporterAmountChartDTO;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;

@Repository
public interface TenantManufactureRepository extends JpaRepository<TenantManufacture, String> {

	public List<TenantManufacture> findByTenantId(String tenantId);
	
	@Query(value ="SELECT * FROM tbl_tenant_manufacture where status = 'ACTIVE' and tenant_id =:tenantId",nativeQuery = true)
	public List<TenantManufacture> findByTenantIdAndStatus(String tenantId);
	
	@Query(value ="select * from tbl_tenant_manufacture tm left join tbl_tenant t on t.tenant_id = tm.tenant_id where t.status = 'APPROVED' AND tm.status= 'ACTIVE'",nativeQuery = true)
	public List<TenantManufacture> findByTenantIdAndStatus();
	

	@Query(value ="SELECT tm.*, m.manufacturer_name FROM tbl_tenant_manufacture tm JOIN tbl_manufacturer m ON tm.manufacturer_id = m.manufacturer_id WHERE tm.tenant_id=:id And tm.status='ACTIVE' AND m.status='ACTIVE' order BY m.manufacturer_name ASC ",nativeQuery = true)
	List<TenantManufacture> getTenantManufacturesByIdByStatus(@Param("id") String id);
	
	@Query(value ="SELECT tm.manufacturer_id AS manufacturerId,m.manufacturer_name AS manufacturerName FROM tbl_tenant_manufacture tm JOIN tbl_manufacturer m ON tm.manufacturer_id = m.manufacturer_id WHERE tm.tenant_id=:id And tm.status='ACTIVE' AND m.status='ACTIVE' order BY m.manufacturer_name ASC ",nativeQuery = true)
	List<TransporterAmountChartDTO> getTenantManufacturesByIdBy(@Param("id") String id);
	
//	@Query(value ="SELECT tm.manufacturer_id AS manufacturerId,m.manufacturer_name AS manufacturerName FROM tbl_tenant_manufacture tm JOIN tbl_manufacturer m ON tm.manufacturer_id = m.manufacturer_id WHERE tm.tenant_id=:id And tm.status='ACTIVE' AND m.status='ACTIVE' order BY m.manufacturer_name ASC ",nativeQuery = true)
//	List<TransporterAmountChartDTO> getTenantManufacturesByIdBy(@Param("id") String id);
	
	@Query(value ="SELECT tm.*, m.manufacturer_name FROM tbl_tenant_manufacture tm JOIN tbl_manufacturer m ON tm.manufacturer_id = m.manufacturer_id WHERE tm.tenant_id=:id",nativeQuery = true)
	List<TenantManufacture> getTenantManufacturesById(@Param("id") String id);

	@Query(value="SELECT * FROM tbl_tenant_manufacture WHERE manufacturer_id =:manufacturerId AND tenant_id = :tenantId AND status='ACTIVE'",nativeQuery = true)
	public Optional<TenantManufacture> findByManufactureId(@Param("manufacturerId") String manufacturerId,@Param("tenantId") String tenantId);
	
	@Query(value="SELECT * FROM tbl_tenant_manufacture WHERE manufacturer_id =:manufacturerId AND tenant_id = :tenantId",nativeQuery = true)
	public Optional<TenantManufacture> findByManufactureIdNotStatus(@Param("manufacturerId") String manufacturerId,@Param("tenantId") String tenantId);
	
	@Query(value="SELECT * FROM tbl_tenant_manufacture WHERE manufacturer_id =:manufacturerId AND tenant_id = :tenantId AND status ='ACTIVE' LIMIT 1",nativeQuery = true)
	public Optional<TenantManufacture> findByManufactureIdAndTenantAndStatus(@Param("manufacturerId") String manufacturerId,@Param("tenantId") String tenantId);


	
	@Query(value="SELECT mrp.manufacturer_id,m.manufacturer_name FROM tbl_tenant_manufacture tm LEFT JOIN tbl_manufacturer_report_mapping mrp ON mrp.manufacturer_id = tm.manufacturer_id  LEFT JOIN tbl_manager_manufacturer_mapping mp ON mp.manufacturerid = mrp.manufacturer_id JOIN tbl_manufacturer m ON m.manufacturer_id = mrp.manufacturer_id WHERE tm.tenant_id =:tenantId AND mp.user_id =:userId and tm.status='ACTIVE' group by m.manufacturer_name,mrp.manufacturer_id ",nativeQuery = true)
	public List<Object[]> listvalueManufacturer(String tenantId, String userId);

	
	@Query(value="select t.tenant_id,t.tenant_name from tbl_tenant t left join tbl_tenant_manufacture tm on tm.tenant_id=t.tenant_id where tm.manufacturer_id=:userId group by t.tenant_id,t.tenant_name ",nativeQuery = true)
	public List<Object[]> tenantByManufacturer(String userId);

     @Query(value="SELECT mrp.manufacturer_id,m.manufacturer_name FROM tbl_tenant_manufacture tm LEFT JOIN tbl_manufacturer_report_mapping mrp ON mrp.manufacturer_id = tm.manufacturer_id   JOIN tbl_manufacturer m ON m.manufacturer_id = mrp.manufacturer_id WHERE tm.tenant_id =:tenantId  and tm.status='ACTIVE' group by m.manufacturer_name,mrp.manufacturer_id",nativeQuery = true)
	public List<Object[]> listvalueManufacturerSuper(String tenantId);


	@Query(value ="SELECT tm.* FROM tbl_ticket_order_invoice toi LEFT JOIN tbl_ticket t ON toi.ticket_id = t.ticket_id LEFT JOIN tbl_tenant_manufacture tm ON t.manufacturer_id = tm.manufacturer_id WHERE toi.invoice_number =:invoice AND tm.tenant_id =:tenantId",nativeQuery = true)
	public Optional<TenantManufacture> findByInvoiceNumber(String invoice , String tenantId);
	
	@Query(value = "SELECT tm.* FROM tbl_manager_manufacturer_mapping mm LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = mm.manufacturerid AND tm.tenant_id = mm.tenant_id WHERE mm.user_id =:userId AND mm.tenant_id =:tenantId AND tm.is_wmsed IS TRUE LIMIT 1", nativeQuery = true)
	public Optional<TenantManufacture> findWmsByUserId(String userId, String tenantId);


}
