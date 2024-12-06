package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, String> {
	
	@Query(value="SELECT * from tbl_items where invoice_id=:inv",nativeQuery = true)
	List<Items> findByInvoiceNumber(String inv);
	
	@Query(value="SELECT * FROM tbl_items it LEFT JOIN tbl_invoice i ON i.invoice_number = it.invoice_id WHERE i.tenant_id =:tenantId AND (it.invoice_id ILIKE '%' || :search || '%' OR :search IS NULL)",nativeQuery = true)
	List<Items> findItemsByInvoiceNumber(String tenantId,String search);
	
	@Query(value="SELECT * FROM tbl_items it LEFT JOIN tbl_invoice i ON i.invoice_number = it.invoice_id WHERE i.tenant_id =:tenantId AND i.manufacturer_id=:manufacturerId AND (it.invoice_id ILIKE %:search%)",nativeQuery = true)
	List<Items> findItemsByInvoiceNumberForInvoiceUser(String tenantId,String manufacturerId,String search);
	
	

}
