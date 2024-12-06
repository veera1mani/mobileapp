package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.dto.TicketDEMO;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;

@Repository
public interface TicketOrderInvoiceRepository extends BaseRepository<TicketOrderInvoice,String> {
	

	
	public TicketOrderInvoice getByTicketId(String ticketId);
	
	@Query(value = "select * from tbl_ticket_order_invoice where ticket_id = :ticketId AND invoice_number = :invoiceNumber",nativeQuery = true)
	public TicketOrderInvoice getInvoie(@Param("ticketId") String tenantId,@Param("invoiceNumber") String invoiceNumber);
	
	@Query(value = "select * from tbl_ticket_order_invoice i left join tbl_ticket_order o ON i.ticket_id=o.ticket_id where o.tenant_id = :tenantId",nativeQuery = true)
	public List<TicketOrderInvoice> getAllInvoie(@Param("tenantId") String tenantId);

	@Query(value = "select distinct i.*,o.*,t.*,s.*  from  tbl_ticket_order_invoice i left join tbl_ticket t On i.ticket_id = t.ticket_id left join tbl_ticket_order o On o.ticket_id = i.ticket_id  left join  tbl_manager_manufacturer_mapping b on t.manufacturer_id =b.manufacturerid LEFT JOIN tbl_manufacturer m ON m.manufacturer_id = t.manufacturer_id LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = m.manufacturer_id And tm.tenant_id =:tenantId LEFT JOIN tbl_stockist s ON s.stockist_id = t.stockist_id LEFT JOIN tbl_city ct ON city_code =s.city_id  where b.user_id=:userId And t.tenant_id =:tenantId AND m.status = 'ACTIVE' and tm.status='ACTIVE' And i.status='INVOICE CREATED' AND i.is_selected = false AND (t.ticket_id ILIKE %:search% OR i.invoice_number ILIKE %:search% OR i.num_of_cases ILIKE %:search%  OR i.line_item ILIKE %:search% OR i.invoice_value ILIKE %:search% OR s.stockist_name ILIKE %:search% OR ct.city_name ILIKE %:search%) ",nativeQuery = true)
	public List<TicketOrderInvoice> getInvoieceList(@Param("userId") String userId,@Param("tenantId") String tenantId , String search);
	
	@Query(value = "select s.stockist_name as stockistName,ct.city_name as city from  tbl_stockist s LEFT JOIN tbl_city ct ON city_code =s.city_id LEFT JOIN tbl_ticket t ON t.stockist_id = s.stockist_id Where t.tenant_id =:tenantId AND t.ticket_id=:ticketId",nativeQuery = true)
	public Optional<TicketDEMO> getStockistNameAndLocationByTicketId(@Param("ticketId") String ticketId,@Param("tenantId") String tenantId);
	
	@Query(value = "SELECT distinct toi.*, toa.*, s.*,ct.* FROM tbl_ticket_order_invoice toi LEFT JOIN tbl_ticket_order toa ON toi.ticket_id = toa.ticket_id LEFT JOIN tbl_ticket t on t.ticket_id=toa.ticket_id  LEFT JOIN tbl_stockist s ON t.stockist_id=s.stockist_id left join tbl_city ct on ct.city_code=s.city_id left join tbl_manager_manufacturer_mapping b on t.manufacturer_id =b.manufacturerid  LEFT JOIN tbl_manufacturer m ON m.manufacturer_id =t.manufacturer_id LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = m.manufacturer_id  WHERE toi.status ='INVOICE CREATED' AND toa.tenant_id=:tenantId and b.user_id=:userId and m.status ='ACTIVE' and tm.status='ACTIVE' AND toi.is_selected =true and (s.stockist_name ILIKE %:search% OR toi.invoice_number ILIKE %:search% OR ct.city_name ILIKE  %:search% ) ",nativeQuery = true)
	public List<TicketOrderInvoice> getPickedList(String tenantId, String userId, String search);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice toi LEFT JOIN tbl_ticket_order tio ON tio.ticket_id = toi.ticket_id LEFT JOIN tbl_ticket_status_history tsh ON tsh.ticket_id = toi.ticket_id WHERE tio.tenant_id =:tenantId AND toi.status='DELIVERED' AND tio.pod_required='true' AND (:search IS NULL OR toi.ticket_id ILIKE '%' || :search || '%' OR toi.invoice_number ILIKE '%' || :search || '%') ",nativeQuery = true)
	public List<TicketOrderInvoice> getPodList(String tenantId,String search);
	
	@Query(value = "SELECT toi.ticket_id,toi.invoice_number,toi.num_of_cases,s.stockist_name,c.city_name,toi.status FROM tbl_ticket_order_invoice toi LEFT JOIN tbl_ticket t ON t.ticket_id = toi.ticket_id LEFT JOIN tbl_ticket_order tio ON tio.ticket_id = toi.ticket_id LEFT JOIN tbl_stockist s ON s.stockist_id = t.stockist_id LEFT JOIN tbl_city c ON c.city_code=s.city_id WHERE tio.tenant_id =:tenantId AND toi.status='DELIVERED' AND tio.pod_required='true' AND (:search IS NULL OR toi.ticket_id ILIKE '%' || :search || '%' OR toi.invoice_number ILIKE '%' || :search || '%') ",nativeQuery = true)
	public List<Object[]> getPodRequiredList(String tenantId,String search);
	
	@Query(value ="SELECT distinct toi.* from tbl_ticket_order_invoice toi join tbl_ticket_order toa on toi.ticket_id = toa.ticket_id left join tbl_ticket t on toi.ticket_id = t.ticket_id left join tbl_manager_manufacturer_mapping b on b.manufacturerid = t.manufacturer_id and b.tenant_id =:tenantId and b.user_id =:userId left join tbl_manufacturer m on t.manufacturer_id = m.manufacturer_id left join tbl_tenant_manufacture tm on tm.manufacturer_id = m.manufacturer_id and tm.tenant_id =:tenantId left join tbl_stockist s on s.stockist_id = t.stockist_id left join tbl_city ct on ct.city_code = s.city_id where toi.status = 'PACKED' AND toi.tenant_id =:tenantId AND ( NOT EXISTS (SELECT 1 FROM tbl_order_transporter_mapping t WHERE t.ticket_id = toi.ticket_id)) AND  (s.stockist_name ILIKE %:search% OR toi.invoice_number ILIKE %:search% OR toi.ticket_id  ILIKE %:search% OR ct.city_name ILIKE  %:search%  OR toi.num_of_cases ILIKE %:search% )  ",nativeQuery = true)
	public List<TicketOrderInvoice> getDispatchedList(@Param("tenantId") String tenantId,@Param("userId") String userId,String search);

	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where invoice_number=:invoiceNumber AND tenant_id=:tenantId",nativeQuery = true)
	public List<TicketOrderInvoice> findByInvoiceNumber(String invoiceNumber,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where LOWER(REPLACE(invoice_number,' ','')) =:invoiceNumber AND tenant_id=:tenantId",nativeQuery = true)
	public Optional<TicketOrderInvoice> findByInvoiceNumberAndTenantId(String invoiceNumber,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where invoice_number =:invoiceNumber AND tenant_id=:tenantId",nativeQuery = true)
	public Optional<TicketOrderInvoice> findByInvoiceAndTenantId(String invoiceNumber,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where invoice_number=:invoiceNumber AND tenant_id=:tenantId",nativeQuery = true)
	public Optional<TicketOrderInvoice> findByInvoiceNumbeForPicklist(String invoiceNumber,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where invoice_number=:invoiceNumber and ticket_id=:ticketId and tenant_id=:tenantId",nativeQuery = true)
	public Optional<TicketOrderInvoice> findByInvoice(String invoiceNumber ,String ticketId,String tenantId );
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where pack_id=:packId and tenant_id=:tenantId",nativeQuery = true)
	public List<TicketOrderInvoice> findByPickId(String packId,String tenantId );
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where transporter =:transporter  and tenant_id=:tenantId AND status='TRANSPORTER ASSIGNED'",nativeQuery = true)
	public List<TicketOrderInvoice> findByTransporterAndVehicaleNo(String transporter,String tenantId );
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where ticket_id=:ticketId AND tenant_id=:tenantId ORDER BY id",nativeQuery = true)
	public List<TicketOrderInvoice> findByTicketId(String ticketId,String tenantId);
	
	@Query(value = "SELECT tr.transport_name,i.vehicale_no,STRING_AGG(i.invoice_number, ', ') AS grouped_invoice_numbers,i.lr_number,i.lr_recieve_date,i.lr_document,i.address,i.pack_id,i.delivery_date FROM tbl_ticket_order_invoice i LEFT JOIN tbl_transport tr ON tr.transport_id = i.transporter where i.ticket_id=:ticketId AND i.tenant_id=:tenantId AND i.pack_id IS NOT null GROUP BY i.lr_document,i.lr_number,i.lr_recieve_date,i.pack_id,tr.transport_name,i.vehicale_no,i.address,i.delivery_date",nativeQuery = true)
	public List<Object[]> findByTicketIdForLrDocument(String ticketId,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where ticket_id=:ticketId AND tenant_id=:tenantId AND priority=false ORDER BY id",nativeQuery = true)
	public List<TicketOrderInvoice> findByTicketIdNotpriority(String ticketId,String tenantId);
	   

	@Query(value = "SELECT * FROM tbl_ticket_order_invoice where select_list=:selectList AND tenant_id=:tenantId",nativeQuery = true)
	public List<TicketOrderInvoice> findInvoiceBySelectList(String selectList,String tenantId);
	
	@Query(value = "SELECT invoice_number FROM tbl_ticket_order_invoice where select_list=:selectList AND tenant_id=:tenantId",nativeQuery = true)
	public List<String> findInvoiceNumbetBySelectList(String selectList,String tenantId);
	
	 @Query(value ="SELECT select_list FROM tbl_ticket_order_invoice where (select_list IS NOT NULL AND select_list != '')  ORDER BY CAST(select_list AS INTEGER) DESC limit 1",nativeQuery = true)
	 public String getLastSequence();
	 
	 @Query(value = "SELECT * FROM tbl_ticket_order_invoice i left join tbl_ticket tc ON tc.ticket_id = i.ticket_id where tc.stockist_id=:stockistId AND tc.tenant_id=:tenantId AND (i.status='PACKED' OR i.status='TRANSPORTER ASSIGNED' ) AND i.priority=false AND i.pack_id IS NOT null LIMIT 1 ",nativeQuery = true)
	 public Optional<TicketOrderInvoice> findByStockistAnsStatusNotpriority(@Param("stockistId") String stockistId,@Param("tenantId") String tenantId);
	 
	 @Query(value = "SELECT * FROM tbl_ticket_order_invoice i left join tbl_ticket tc ON tc.ticket_id = i.ticket_id where tc.stockist_id=:stockistId AND tc.tenant_id=:tenantId AND i.status='PACKED' AND i.priority=false AND i.pack_id IS NOT null",nativeQuery = true)
	 public List<TicketOrderInvoice> findByStockistAndStatusNotpriority(@Param("stockistId") String stockistId,@Param("tenantId") String tenantId);

	 @Query(value = "SELECT i.num_of_cases from tbl_ticket_order_invoice i where i.tenant_id =:tenantId and i.pack_id =:packId",nativeQuery = true)
	 public List<String> findNoOfCases(@Param("packId") String packId,@Param("tenantId") String tenantId);
	 
	 @Query(value = "SELECT * from tbl_ticket_order_invoice i where i.tenant_id =:tenantId and i.pack_id =:packId",nativeQuery = true)
	 public List<TicketOrderInvoice> findMergeInvoice(@Param("packId") String packId,@Param("tenantId") String tenantId);

	 @Query(value = "SELECT STRING_AGG(i.invoice_number, ', ') AS invoice_numbers,SUM(CAST(i.num_of_cases AS DECIMAL)) AS total_cases ,MAX(s.stockist_name) AS stockist, m.manufacturer_name as manufacturer,MAX(c.city_name) AS location, MAX(i.status) AS status,MAX(i.assign_transport) AS assignedOn  FROM tbl_ticket_order_invoice i LEFT JOIN tbl_ticket t ON t.ticket_id = i.ticket_id LEFT JOIN tbl_stockist s ON t.stockist_id = s.stockist_id LEFT JOIN tbl_city c ON s.city_id = c.city_code LEFT JOIN tbl_manufacturer m ON t.manufacturer_id = m.manufacturer_id WHERE i.transporter =:userId and i.tenant_id =:tenantId  AND (i.status = 'DISPATCHED' or i.status = 'DELIVERED' or i.status = 'POD RECEIVED' or i.status = 'TRANSPORTER ASSIGNED' ) AND i.status ILIKE %:status% AND t.stockist_id ILIKE %:stockist% AND t.manufacturer_id ILIKE %:manufacturer% AND (i.invoice_number ILIKE %:search% OR i.num_of_cases ILIKE %:search% OR s.stockist_name ILIKE %:search% OR m.manufacturer_name ILIKE %:search% OR c.city_name ILIKE %:search% OR i.status ILIKE %:search% ) AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED') THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) GROUP BY pack_id, m.manufacturer_name, i.created_on",nativeQuery = true)
	 public List<Object[]> getTransporterOrders(@Param("userId") String userId,@Param("tenantId") String tenantId,String search , String status , Pageable pageable, String stockist , String manufacturer);
	 
	 @Query(value = "SELECT STRING_AGG(i.invoice_number, ', ') AS invoice_numbers,SUM(CAST(i.num_of_cases AS DECIMAL)) AS total_cases ,MAX(s.stockist_name) AS stockist, m.manufacturer_name as manufacturer,MAX(c.city_name) AS location, MAX(i.status) AS status,MAX(i.assign_transport) AS assignedOn FROM tbl_ticket_order_invoice i LEFT JOIN tbl_ticket t ON t.ticket_id = i.ticket_id LEFT JOIN tbl_stockist s ON t.stockist_id = s.stockist_id LEFT JOIN tbl_city c ON s.city_id = c.city_code LEFT JOIN tbl_manufacturer m ON t.manufacturer_id = m.manufacturer_id WHERE i.transporter =:userId and i.tenant_id =:tenantId   AND (i.status = 'DISPATCHED' or i.status = 'DELIVERED' or i.status = 'POD RECEIVED' or i.status = 'TRANSPORTER ASSIGNED') AND i.status ILIKE %:status% AND t.stockist_id ILIKE %:stockist% AND t.manufacturer_id ILIKE %:manufacturer%  AND (i.invoice_number ILIKE %:search% OR i.num_of_cases ILIKE %:search% OR s.stockist_name ILIKE %:search% OR m.manufacturer_name ILIKE %:search% OR c.city_name ILIKE %:search% OR i.status ILIKE %:search% )AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED') THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) GROUP BY pack_id, m.manufacturer_name, i.created_on ",nativeQuery = true)
	 public List<Object[]> getTransporterOrders(@Param("userId") String userId,@Param("tenantId") String tenantId , String search, String status,String stockist , String manufacturer);

	 @Query(value = "SELECT STRING_AGG(i.invoice_number, ', ') AS invoice_numbers,SUM(CAST(i.num_of_cases AS DECIMAL)) AS total_cases ,MAX(s.stockist_name) AS stockist, m.manufacturer_name as manufacturer,MAX(c.city_name) AS location, MAX(i.status) AS status,MAX(i.assign_transport) AS assignedOn,MAX(tr.transport_name) AS transporter  FROM tbl_ticket_order_invoice i LEFT JOIN tbl_ticket t ON t.ticket_id = i.ticket_id LEFT JOIN tbl_stockist s ON t.stockist_id = s.stockist_id LEFT JOIN tbl_city c ON s.city_id = c.city_code LEFT JOIN tbl_manufacturer m ON t.manufacturer_id = m.manufacturer_id LEFT JOIN tbl_transport tr ON tr.transport_id = i.transporter WHERE  i.tenant_id =:tenantId  AND (i.status = 'DISPATCHED' or i.status = 'DELIVERED' or i.status = 'POD RECEIVED' or i.status = 'TRANSPORTER ASSIGNED') AND i.status ILIKE %:status% AND t.stockist_id ILIKE %:stockist% AND t.manufacturer_id ILIKE %:manufacturer% AND i.transporter  ILIKE %:trans% AND (i.invoice_number ILIKE %:search% OR i.num_of_cases ILIKE %:search% OR s.stockist_name ILIKE %:search% OR m.manufacturer_name ILIKE %:search% OR c.city_name ILIKE %:search% OR i.status ILIKE %:search% ) AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED') THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) GROUP BY pack_id, m.manufacturer_name, i.created_on",nativeQuery = true)
	 public List<Object[]> getAllTransporterOrders(@Param("tenantId") String tenantId,String search , String status , Pageable pageable, String stockist , String manufacturer, String trans);
	 
	 @Query(value = "SELECT STRING_AGG(i.invoice_number, ', ') AS invoice_numbers,SUM(CAST(i.num_of_cases AS DECIMAL)) AS total_cases ,MAX(s.stockist_name) AS stockist, m.manufacturer_name as manufacturer,MAX(c.city_name) AS location, MAX(i.status) AS status,MAX(i.assign_transport) AS assignedOn,MAX(tr.transport_name) AS transporter FROM tbl_ticket_order_invoice i LEFT JOIN tbl_ticket t ON t.ticket_id = i.ticket_id LEFT JOIN tbl_stockist s ON t.stockist_id = s.stockist_id LEFT JOIN tbl_city c ON s.city_id = c.city_code LEFT JOIN tbl_manufacturer m ON t.manufacturer_id = m.manufacturer_id LEFT JOIN tbl_transport tr ON tr.transport_id = i.transporter WHERE  i.tenant_id =:tenantId   AND (i.status = 'DISPATCHED' or i.status = 'DELIVERED' or i.status = 'POD RECEIVED' or i.status = 'TRANSPORTER ASSIGNED') AND i.status ILIKE %:status% AND t.stockist_id ILIKE %:stockist% AND t.manufacturer_id ILIKE %:manufacturer% AND i.transporter  ILIKE %:trans%  AND (i.invoice_number ILIKE %:search% OR i.num_of_cases ILIKE %:search% OR s.stockist_name ILIKE %:search% OR m.manufacturer_name ILIKE %:search% OR c.city_name ILIKE %:search% OR i.status ILIKE %:search% ) AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED') THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) GROUP BY pack_id, m.manufacturer_name, i.created_on ",nativeQuery = true)
	 public List<Object[]> getAllTransporterOrders(@Param("tenantId") String tenantId , String search, String status,String stockist , String manufacturer, String trans);

	 @Query(value = "SELECT COUNT(CASE WHEN i.status = 'DELIVERED' OR i.status = 'DISPATCHED' OR i.status = 'POD RECEIVED' OR i.status = 'TRANSPORTER ASSIGNED' THEN 1 END) AS invoices, COUNT(CASE WHEN i.status = 'DISPATCHED' THEN 1 END) AS dispatched , COUNT(CASE WHEN i.status = 'DELIVERED' OR i.status = 'POD RECEIVED' THEN 1 END) AS delivered , SUM(CASE WHEN i.status = 'DELIVERED' OR i.status = 'DISPATCHED' OR i.status = 'POD RECEIVED' OR i.status = 'TRANSPORTER ASSIGNED' THEN CAST(i.num_of_cases AS DECIMAL) ELSE 0 END) AS total_cases, COUNT(CASE WHEN i.status = 'TRANSPORTER ASSIGNED' THEN 1 END) AS assigned FROM tbl_ticket_order_invoice i WHERE i.transporter =:userId AND i.tenant_id =:tenantId AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED' ) THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END)",nativeQuery = true)
	 public List<Object[]> getTransporterOrdersCount(@Param("userId") String userId,@Param("tenantId") String tenantId);

	 @Query(value = "SELECT COUNT(CASE WHEN i.status = 'DELIVERED' OR i.status = 'DISPATCHED' OR i.status = 'POD RECEIVED' OR i.status = 'TRANSPORTER ASSIGNED' THEN 1 END) AS invoices, COUNT(CASE WHEN i.status = 'DISPATCHED' THEN 1 END) AS dispatched , COUNT(CASE WHEN i.status = 'DELIVERED' OR i.status = 'POD RECEIVED' THEN 1 END) AS delivered , SUM(CASE WHEN i.status = 'DELIVERED' OR i.status = 'DISPATCHED' OR i.status = 'POD RECEIVED' OR i.status = 'TRANSPORTER ASSIGNED' THEN CAST(i.num_of_cases AS DECIMAL) ELSE 0 END) AS total_cases, COUNT(CASE WHEN i.status = 'TRANSPORTER ASSIGNED' THEN 1 END) AS assigned  FROM tbl_ticket_order_invoice i WHERE i.tenant_id =:tenantId AND(CASE WHEN (i.status = 'DELIVERED' or i.status = 'POD RECEIVED') THEN (EXTRACT(YEAR FROM i.assign_transport) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM i.assign_transport) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END)",nativeQuery = true)
	 public List<Object[]> getAllTransporterOrdersCount(@Param("tenantId") String tenantId);
	 
	 @Query(value = "select manufacturer_name from tbl_manufacturer where manufacturer_id in(select manufacturer_id from tbl_ticket where ticket_id=:ticketId and tenant_id =:tenantId)",nativeQuery=true)
	 public String findManfNameByTicketId(@Param("ticketId") String ticketId , @Param("tenantId") String tenantId);

	 @Query(value = "select * from tbl_ticket_order_invoice where status = 'PICKED' and tenant_id=:tenantId",nativeQuery = true)
	 public List<TicketOrderInvoice> getPickedByTenant(String tenantId);
}
