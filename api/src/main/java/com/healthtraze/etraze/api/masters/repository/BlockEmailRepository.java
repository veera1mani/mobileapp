package com.healthtraze.etraze.api.masters.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.BlockEmail;

@Repository
public interface BlockEmailRepository extends BaseRepository<BlockEmail,String>{
	
	@Query(value = "select block_value from tbl_block_email where block_type =:blockType AND tenant_manufacturer_id =:tenantManufacturerId AND tenant_id=:tenantId" , nativeQuery = true)
    List<String> findAllBlockEmailByManufacturerAndType(@Param("blockType") String blockType,@Param("tenantManufacturerId") String tenantManufacturerId,@Param("tenantId") String tenantId);    
	
	@Query(value = "select be.block_type AS blockType,be.block_value AS blockValue,m.manufacturer_name AS manufacturerName,be.id AS id from tbl_block_email be left join tbl_manufacturer m ON m.manufacturer_id = be.manufacturer_id where be.tenant_id=:tenantId", nativeQuery = true)
    List<Object[]> findAllBlockEmailByManufacturer(@Param("tenantId") String tenantId);
    
    @Query(value = "select * from tbl_block_email where id =:id AND tenant_id=:tenantId", nativeQuery = true)
    Optional<BlockEmail> findByIdAndTenantId(@Param("id") String id,@Param("tenantId") String tenantId);

}
