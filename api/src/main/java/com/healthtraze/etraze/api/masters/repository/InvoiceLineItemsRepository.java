package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.InvoiceLineItems;

@Repository
public interface InvoiceLineItemsRepository extends  BaseRepository<InvoiceLineItems, String> {

	
	@Query(value="SELECT * from tbl_invoice_line_items where invoice_id=:inv",nativeQuery = true)
	List<InvoiceLineItems> findByInvoiceNumber(String inv);
	
	@Query(value="SELECT * from tbl_invoice_line_items where invoice_id=:inv and partial_pick = false",nativeQuery = true)
	List<InvoiceLineItems> findByInvoiceNumberForPick(String inv);
	
	@Query(value="SELECT * from tbl_invoice_line_items where invoice_id=:inv and partial_check = false",nativeQuery = true)
	List<InvoiceLineItems> findByInvoiceNumberForCheck(String inv);
	
	@Query(value="SELECT * from tbl_invoice_line_items where invoice_id=:inv and product_code=:productCode and tenant_id=:tenantId ",nativeQuery = true)
	Optional<InvoiceLineItems> findByInvoiceAndCode(String inv, String productCode , String tenantId);
	
	@Query(value="SELECT * FROM tbl_invoice_line_items li LEFT JOIN tbl_invoice i ON i.invoice_number = li.invoice_id LEFT JOIN tbl_product pr ON pr.product_code = li.product_code AND pr.manufacturer = i.manufacturer_id AND pr.tenant_id = li.tenant_id WHERE li.invoice_id =:inv AND pr.ware_house_location =:floor",nativeQuery = true)
	List<InvoiceLineItems> findByInvoiceAndFloor(String inv, String floor);
	
	@Query(value="SELECT * FROM tbl_invoice_line_items li LEFT JOIN tbl_invoice i ON i.invoice_number = li.invoice_id LEFT JOIN tbl_product pr ON pr.product_code = li.product_code AND pr.manufacturer = i.manufacturer_id AND pr.tenant_id = li.tenant_id WHERE li.invoice_id =:inv AND pr.ware_house_location =:floor AND partial_pick = false ",nativeQuery = true)
	List<InvoiceLineItems> findByInvoiceAndFloorByUser(String inv, String floor);
}
