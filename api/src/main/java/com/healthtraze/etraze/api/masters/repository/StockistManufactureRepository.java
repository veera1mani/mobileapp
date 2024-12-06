package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;

@Transactional
@Repository
public interface StockistManufactureRepository extends BaseRepository<StockistManufacture, Long> {
	
	@Query(value = "select * from tbl_stockist_manufacture where sap_id = :sapId And stockist_id=:stockistId And tenant_id=:tenantId",nativeQuery = true)
	Optional<StockistManufacture> findBySap(@Param("sapId") String sapId,@Param("stockistId") String stockistId,@Param ("tenantId") String tenantId);  
	
	
	@Query(value = "select distinct sm.id, sm.cheque_category, sm.credit_days, sm.isenable, sm.location, sm.manufacture, sm.manufacture_name, sm.sap_id, sm.stockist_id, sm.threshold, sm.tlt,sm.tenant_id from tbl_stockist_manufacture sm left join tbl_tenant_manufacture tm  ON tm.manufacturer_id = sm.manufacture and tm.tenant_id=sm.tenant_id left join tbl_manufacturer m ON m.manufacturer_id = sm.manufacture where sm.isenable = 'true' And tm.status='ACTIVE' AND m.status='ACTIVE' And sm.stockist_id=:stockistId And sm.tenant_id=:tenantId",nativeQuery = true)
	List<StockistManufacture> findListByStockist(String stockistId,String tenantId);
	
	@Modifying
	@Query(value = "UPDATE tbl_stockist_manufacture SET isenable=false WHERE id =:id And tenant_id=:tenantId",nativeQuery = true)
	void deletestkmapping(@Param("id") Long id,@Param("tenantId") String tenantId);
	
	@Query(value = "select * from tbl_stockist_manufacture where manufacture = :manufacturer And stockist_id=:stockistId And tenant_id=:tenantId",nativeQuery = true)
	Optional<StockistManufacture> findByManufacturer(@Param("manufacturer") String manufacturer,@Param("stockistId") String stockistId,@Param ("tenantId") String tenantId);  


	@Query(value="SELECT * FROM tbl_stockist_manufacture WHERE sap_id =:sap AND tenant_id = :tenantId",nativeQuery = true)
	 public Optional<StockistManufacture> findStockistBySapAndTenant(String sap, String tenantId);
	
	@Query(value = "select sm.sap_id from tbl_stockist_manufacture sm  where sm.isenable = 'true' AND sm.tenant_id=:tenantId AND sm.manufacture IN (:manList)",nativeQuery = true)
	List<String> findAllStockistMan(String tenantId, List<String> manList);


}
