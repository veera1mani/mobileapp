package com.healthtraze.etraze.api.masters.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.TempInvoice;

@Repository
public interface TempInvoiceRepository extends BaseRepository<TempInvoice, Long> {

	@Query(value = "select * from tbl_temp_invoice where LOWER(REPLACE(invoice_number,' ','')) =:inv and tenant_id =:tenantId", nativeQuery = true)
	public Optional<TempInvoice> findByInvoiceNumber(String inv, String tenantId);
}
