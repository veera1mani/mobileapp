package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>{
	@Query(value="SELECT * FROM tbl_invoice WHERE tenant_id = :tenantId",nativeQuery = true)
    List<Invoice> findByInvoiceNumber(String tenantId);
}
