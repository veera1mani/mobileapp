package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ManagerManufacturerMapping;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;

public interface ManagerManufacturerMappingRepository extends BaseRepository<ManagerManufacturerMapping, String> {

	@Query(value ="SELECT DISTINCT mm.id, mm.manufacture_name, mm.manufacturerid, mm.user_id,mm.tenant_id \r\n"
			+ "FROM tbl_manager_manufacturer_mapping mm\r\n"
			+ "INNER JOIN tbl_manufacturer m ON mm.manufacturerid = m.manufacturer_id AND m.status = 'ACTIVE'\r\n"
			+ "INNER JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = m.manufacturer_id AND tm.status = 'ACTIVE'\r\n"
			+ "WHERE mm.user_id = :userId AND tm.tenant_id = :tenantId", nativeQuery = true)

	public List<ManagerManufacturerMapping> findManufacturBYUserId(@Param("userId") String userId,@Param("tenantId") String tenantId);
	
	
	@Query(value ="select manufacturerid from tbl_manager_manufacturer_mapping mm LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = mm.manufacturerid AND tm.tenant_id = mm.tenant_id where mm.tenant_id=:tenantId AND mm.user_id =:userId AND tm.status='ACTIVE'", nativeQuery = true)
	public List<String> findAllManufacturBYUserId(@Param("userId") String userId,@Param("tenantId") String tenantId);

    @Query(value="WITH CTE AS (SELECT tableB.*, ROW_NUMBER() OVER (PARTITION BY tableB.manufacturerid ORDER BY tableB.manufacturerid) AS rn FROM tbl_manager_manufacturer_mapping AS tableB JOIN tbl_tenant_manufacture AS tableA ON tableA.manufacturer_id = tableB.manufacturerid WHERE tableA.tenant_id =:tenantId AND tableB.tenant_id =:tenantId ) SELECT * FROM CTE WHERE rn = 1 ",nativeQuery = true)
	public List<ManagerManufacturerMapping> findManufacturBYUserId(String tenantId);
    
    
    @Query(value ="select * from tbl_manager_manufacturer_mapping where user_id=:userId AND manufacturerid=:manufacturerId AND tenant_id=:tenantId  LIMIT 1", nativeQuery = true)
	public Optional<ManagerManufacturerMapping> findManufacturBYUserId(@Param("userId") String userId,@Param("manufacturerId") String manufacturerId,@Param("tenantId") String tenantId);
	
	

}
