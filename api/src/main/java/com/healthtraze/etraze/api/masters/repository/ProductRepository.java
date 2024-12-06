package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Product;
import com.healthtraze.etraze.api.masters.model.ProductComposite;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductComposite> {

	@Query(value="SELECT * from tbl_product where product_code=:productCode AND tenant_id=:tenantId AND manufacturer=:manufacturer",nativeQuery = true)
	Product getProductByCode(String productCode,String tenantId,String manufacturer);
	
	@Query(value="select * from tbl_product t where tenant_id=:tenantId AND "
			+ "(t.product_code ILIKE %:value% OR "
			+ "t.product_name ILIKE %:value% OR "
			+ "t.manufacturer ILIKE %:value% OR "
			+ "t.ware_house_location ILIKE %:value% OR "
			+ "t.bin ILIKE %:value% OR "
			+ "t.row ILIKE %:value% OR "
			+ "t.pallet ILIKE %:value% )",nativeQuery = true)
	public Page<Product> findByTenantId(Pageable pageable,@Param(value= "tenantId")String tenantId,  String value);
	
	@Query(value="select * from tbl_product where tenant_id=:tenantId",nativeQuery = true)
	public List<Product> findByTenantId(@Param(value = "tenantId") String tenantId);

}
