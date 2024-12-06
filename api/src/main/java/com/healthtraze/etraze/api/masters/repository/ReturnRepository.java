package com.healthtraze.etraze.api.masters.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.Return;

@Repository
public interface ReturnRepository extends BaseRepository<Return, String> {

    Optional<Return> findBySerialNumber(String serialNum);
    
    @Query(value = "select * from tbl_returns where tenant_id = :tenantId AND status ILIKE %:sts% and (claim_number ILIKE %:search% OR stockist_id ILIKE %:search% OR transporter_id ILIKE %:search% OR serial_number ILIKE %:search% OR manufacturer ILIKE %:search% OR status ILIKE %:search% OR created_by ILIKE %:search% OR CAST(received_date AS TEXT) ILIKE %:search%)",nativeQuery = true)
    List<Return> getAllReturns(Pageable pageable,@Param("tenantId") String tenantId,@Param("search") String search,@Param("sts") String sts);
    
    @Query(value = "select * from tbl_returns where tenant_id = :tenantId AND status ILIKE %:sts%",nativeQuery = true)
    List<Return> getAllReturnsByTenant(@Param("tenantId") String tenantId,@Param("sts") String sts);
    
    @Query(value = "select * from tbl_returns where tenant_id = :tenantId AND status='RECEIVED'",nativeQuery = true)
    List<Return> getAllReturnsByTenant(@Param("tenantId") String tenantId);
    
    
    @Query(value ="SELECT MAX(CAST(SUBSTRING(serial_number FROM LENGTH('RN')+1) AS INTEGER)) AS lastSequence FROM tbl_returns",nativeQuery = true)
	public Integer getLastSequence();
    
    @Query(value ="SELECT RIGHT(serial_number,2) FROM tbl_returns where serial_number IS NOT NULL AND tenant_id =:tenantId AND created_on=CURRENT_DATE ORDER BY serial_number DESC limit 1",nativeQuery = true)
   	public String getLastSequence(String tenantId);
    
    @Query(value ="SELECT RIGHT(serial_number, 3)FROM tbl_returns WHERE serial_number IS NOT NULL  AND tenant_id = :tenantId AND manufacturer=:manufacturerId  AND EXTRACT(MONTH FROM TO_DATE(SUBSTRING(serial_number FROM 7 FOR 6), 'YYYYMM')) = EXTRACT(MONTH FROM CURRENT_DATE) ORDER BY serial_number DESC LIMIT 1",nativeQuery = true)
   	public String getLastSequenceByManufacturer(String tenantId,String manufacturerId);
    
    @Query(value = "select * from tbl_returns where claim_number =:claimNumber And tenant_id=:tenantId",nativeQuery = true)
    public Return getByClaimNumber(@Param("claimNumber") String claimNumber,@Param("tenantId") String tenantId);
    
    @Query(value = "select * from tbl_returns where claim_id =:claimId And tenant_id=:tenantId",nativeQuery = true)
    public Optional<Return> findByClaimId(@Param("claimId") String claimId,@Param("tenantId") String tenantId);
    
    @Query(value = "select * from tbl_returns where serial_number =:serialNumber",nativeQuery = true)
    public Optional<Return> getSerialnumber(@Param("serialNumber") String serialNumber);
    
    @Query(value = "select return_id,serial_number from tbl_returns r left join tbl_tenant_manufacture tm on r.manufacturer =tm.manufacturer_id left join tbl_manufacturer m on r.manufacturer =m.manufacturer_id   where r.tenant_id =:tenantId and m.status='ACTIVE' and tm.status='ACTIVE'",nativeQuery = true)
    public List<Object[]> getAllReturnId(@Param("tenantId") String tenantId);
    
    @Query(value = "select COALESCE(SUM(CASE WHEN r.status ='PENDING' THEN 1 ELSE 0 END), 0) AS pending,COALESCE(SUM(CASE WHEN r.status ='RECEIVED' THEN 1 ELSE 0 END), 0) AS recived,COALESCE(SUM(CASE WHEN r.status ='CHECKED' THEN 1 ELSE 0 END), 0) AS checked,COALESCE(SUM(CASE WHEN r.non_salabletatus ='GRRN_CREATED' OR r.salabletatus ='GRRN_CREATED' THEN 1 ELSE 0 END), 0) AS grrnCreated,COALESCE(SUM(CASE WHEN r.status ='CHECKED II' THEN 1 ELSE 0 END), 0) AS Secondcheck,COALESCE(SUM(CASE WHEN r.non_salabletatus ='CN_CREATED' OR r.salabletatus ='CN_CREATED' THEN 1 ELSE 0 END), 0) AS cnCreated from tbl_returns r where tenant_id=:tenantId",nativeQuery = true)
    public List<Object[]> getReturnsCountAll(@Param("tenantId") String tenantId);
    
    @Query(value = "SELECT COALESCE(SUM(CASE WHEN r.status ='PENDING' THEN 1 ELSE 0 END), 0) AS pending, COALESCE(SUM(CASE WHEN r.status ='RECEIVED' THEN 1 ELSE 0 END), 0) AS received, COALESCE(SUM(CASE WHEN r.status ='CHECKED' THEN 1 ELSE 0 END), 0) AS checked, COALESCE(SUM(CASE WHEN r.status ='GRRN_CREATED' THEN 1 ELSE 0 END), 0) AS  grrn, COALESCE(SUM(CASE WHEN r.status ='CHECKED II' THEN 1 ELSE 0 END), 0) AS secondCheck, COALESCE(SUM(CASE WHEN r.status ='CN_CREATED' THEN 1 ELSE 0 END), 0) AS cn, COALESCE(SUM(CASE WHEN r.status IN ('PENDING', 'RECEIVED', 'CHECKED', 'CHECKED II','GRRN_CREATED','CN_CREATED') THEN 1 ELSE 0 END), 0) AS total FROM tbl_returns r LEFT JOIN tbl_manager_manufacturer_mapping mm ON r.manufacturer = mm.manufacturerId WHERE r.tenant_id = :tenantId AND mm.user_id=:userId AND (CASE WHEN r.status = 'CN_CREATED' THEN (EXTRACT(YEAR FROM r.received_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM r.received_date) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END)",nativeQuery = true)
    public List<Object[]> getReturnsCountByManager(@Param("tenantId") String tenantId,@Param("userId") String userId);
    
    @Query(value = "select distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id, a.claim_date,a.claim_number, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname,a.documenturl, a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number,s.stockist_name,ct.city_name from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND tm.tenant_id = :tenantId LEFT JOIN tbl_transport t ON t.transport_id=a.transporter_id LEFT JOIN tbl_stockist s ON s.stockist_id=a.stockist_id LEFT JOIN tbl_city ct ON s.city_id=ct.city_code left join tbl_return_note rn ON rn.return_id = a.return_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status=:status AND (CASE WHEN a.status = 'CN_CREATED' THEN (EXTRACT(YEAR FROM a.created_on) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM a.received_date) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) AND (a.claim_number ILIKE %:search% OR s.stockist_name ILIKE %:search% OR t.transport_name ILIKE %:search% OR a.return_number ILIKE %:search% OR m.manufacturer_name ILIKE %:search%  OR a.created_by ILIKE %:search%  OR a.claim_type ILIKE %:search% OR a.status ILIKE %:search% OR CAST(a.received_date AS TEXT) ILIKE %:search% OR rn.note_number ILIKE %:search% OR ct.city_name ILIKE %:search% )",nativeQuery = true)
    public List<Return> getByManager(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status,@Param("search") String search,Pageable pageable);
    
    @Query(value = "select distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no, a.claim_id,a.claim_date, a.claim_number, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number,s.stockist_name,ct.city_name from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND tm.tenant_id = :tenantId LEFT JOIN tbl_transport t ON t.transport_id=a.transporter_id LEFT JOIN tbl_stockist s ON s.stockist_id=a.stockist_id LEFT JOIN tbl_city ct ON s.city_id=ct.city_code left join tbl_return_note rn ON rn.return_id = a.return_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND (CASE WHEN a.status = 'CN_CREATED' THEN (EXTRACT(YEAR FROM a.created_on) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM a.received_date) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) AND (a.claim_number ILIKE %:search% OR s.stockist_name ILIKE %:search% OR t.transport_name ILIKE %:search% OR a.return_number ILIKE %:search% OR m.manufacturer_name ILIKE %:search%  OR a.created_by ILIKE %:search%  OR a.claim_type ILIKE %:search% OR a.status ILIKE %:search% OR CAST(a.received_date AS TEXT) ILIKE %:search% OR rn.note_number ILIKE %:search% OR ct.city_name ILIKE %:search%)",nativeQuery = true)
    public List<Return> getAllByManager(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("search") String search,Pageable pageable);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id,a.claim_date, a.claim_number, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND tm.tenant_id = :tenantId where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status=:status",nativeQuery = true)
    public List<Return> getByManager(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id,a.claim_date, a.claim_number, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl, a.mobile_document_name,a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number,s.stockist_name from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND tm.tenant_id = :tenantId left join tbl_stockist s on s.stockist_id = a.stockist_id left join tbl_city ct on ct.city_code = s.city_id where b.user_id=:userId and a.tenant_id = :tenantId  and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status !='PENDING' AND a.status != 'RECIVED' AND (a.claim_type='Both' OR a.claim_type='NonSaleable') AND (a.serial_number ILIKE %:search% OR a.claim_number ILIKE %:search% OR a.num_of_non_salable_cases ILIKE %:search% OR s.stockist_name ILIKE %:search% OR ct.city_name ILIKE %:search%  OR  a.claim_type ILIKE %:search% OR a.status ILIKE %:search% ) ",nativeQuery = true)
    public List<Return> getByManagerNotPending(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("search") String search);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id,a.claim_date, a.claim_number, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number,s.stockist_name from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND tm.tenant_id = :tenantId LEFT JOIN tbl_transport t ON t.transport_id=a.transporter_id LEFT JOIN tbl_stockist s ON s.stockist_id=a.stockist_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status=:status AND (CASE WHEN a.status = 'CN_CREATED' THEN (EXTRACT(YEAR FROM a.created_on) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM a.received_date) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) AND (a.claim_number ILIKE %:search% OR s.stockist_name ILIKE %:search% OR t.transport_name ILIKE %:search% OR a.return_number ILIKE %:search% OR m.manufacturer_name ILIKE %:search%  OR a.created_by ILIKE %:search% OR CAST(a.received_date AS TEXT) ILIKE %:search%)",nativeQuery = true)
    public List<Return> getAllByManager(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status,@Param("search") String search);
    
    @Query(value = "select a.return_id, a.return_number,m.manufacturer_name,a.claim_number,s.stockist_name from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id and tm.tenant_id =:tenantId left join tbl_stockist s on s.stockist_id = a.stockist_id  where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status != 'PENDING'",nativeQuery = true)
    public List<Object[]> getAllReturnIdByManager(@Param("userId") String userId,@Param("tenantId") String tenantId);
    
    @Query(value = "select a.return_id, a.return_number,m.manufacturer_name,a.claim_number,s.stockist_name from tbl_returns a left join tbl_manufacturer m on a.manufacturer =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id AND a.tenant_id = tm.tenant_id left join tbl_stockist s on s.stockist_id = a.stockist_id where a.stockist_id =:stockistId AND a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.status != 'PENDING'",nativeQuery = true)
    public List<Object[]> getAllReturnIdByStockist(@Param("stockistId") String stockistId,@Param("tenantId") String tenantId);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id, a.claim_number,a.claim_date, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.non_salabletatus=:status",nativeQuery = true)
    public List<Return> getByUserMobileNS(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no,a.claim_id, a.claim_number,a.claim_date, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND a.salabletatus=:status",nativeQuery = true)
    public List<Return> getByUserMobileS(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no, a.claim_id, a.claim_number,a.claim_date, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE'",nativeQuery = true)
    public List<Return> getByManager(@Param("userId") String userId,@Param("tenantId") String tenantId);
    
    @Query(value = "select  distinct a.return_id, a.serial_number, a.created_by, a.created_on, a.modified_by, a.modified_on, a.tenant_id, a.version_no, a.claim_id, a.claim_number,a.claim_date, a.claim_type, a.lr_booking_date, a.lr_number, a.number_of_boxes, a.number_of_line_items, a.received_date, a.status, a.stockist_id, a.transporter_id, a.manufacturer, a.non_salabletatus, a.salabletatus, a.document, a.docname, a.documenturl,a.mobile_document_name, a.mis_match, a.mis_match_type, a.num_of_non_salable_cases, a.remarks,a.channel,a.return_number from tbl_returns a inner join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacturerId left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id LEFT JOIN tbl_transport t ON t.transport_id=a.transporter_id LEFT JOIN tbl_stockist s ON s.stockist_id=a.stockist_id LEFT JOIN tbl_city ct ON s.city_id=ct.city_code where b.user_id=:userId and a.tenant_id = :tenantId and m.status='ACTIVE' and tm.status='ACTIVE' AND (CASE WHEN a.status = 'CN_CREATED' THEN (EXTRACT(YEAR FROM a.created_on) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM a.received_date) = EXTRACT(MONTH FROM CURRENT_DATE)) ELSE 1=1 END) AND (a.claim_number ILIKE %:search% OR s.stockist_name ILIKE %:search% OR t.transport_name ILIKE %:search% OR a.return_number ILIKE %:search% OR m.manufacturer_name ILIKE %:search%  OR a.created_by ILIKE %:search% OR CAST(a.received_date AS TEXT) ILIKE %:search% OR a.claim_type ILIKE %:search% OR ct.city_name ILIKE %:search% )",nativeQuery = true)
    public List<Return> getAllByManager(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("search") String search);
    
    @Query(value = "select * from tbl_returns where return_id=:returnId AND tenant_id =:tenantId",nativeQuery = true)
    public Optional<Return> getReturnsByTenant(@Param("returnId") String returnId,@Param("tenantId") String tenantId);
    
    @Query(value = "select * from tbl_returns a left join tbl_manager_manufacturer_mapping b on a.manufacturer = b.manufacture_name left join tbl_manufacturer m on b.manufacturerid =m.manufacturer_id  left join tbl_tenant_manufacture tm on m.manufacturer_id =tm.manufacturer_id where b.user_id=:userId And a.tenant_id = :tenantId",nativeQuery = true)
    public List<Return> getByManager1(@Param("userId") String userId,@Param("tenantId") String tenantId);
    
    
    @Query(value="SELECT * FROM (SELECT distinct r.stockist_id AS return_stockist_id, r.claim_number, r.status, r.received_date, CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END AS days_taken, r.lr_booking_date, r.manufacturer FROM tbl_returns r LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer left join tbl_manufacturer mr on mr.manufacturer_id= r.manufacturer and r.tenant_id=tm.tenant_id  WHERE r.tenant_id =:tenantId AND r.status != 'PENDING'  and tm.status='ACTIVE' and mr.status='ACTIVE'  AND CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END > tm.deadline_claim ) AS tableA INNER JOIN (SELECT distinct a.stockist_id AS stockist_id, a.stockist_name, a.city_id, c.city_name, b.sap_id,b.location,b.manufacture FROM tbl_stockist a LEFT JOIN tbl_stockist_manufacture b ON a.stockist_id = b.stockist_id and a.tenant_id=b.tenant_id  LEFT JOIN tbl_manager_manufacturer_mapping mp ON mp.manufacturerid = b.manufacture LEFT JOIN tbl_city c ON a.city_id = c.city_code WHERE a.tenant_id = :tenantId AND mp.user_id = :userId) AS tableB ON tableA.return_stockist_id = tableB.stockist_id and tableA.manufacturer=tableB.manufacture ", nativeQuery = true) 
    List<Object[]> claimsNotClosed(@Param("tenantId") String tenantId,@Param("userId") String userId);
   
    @Query(value="SELECT * FROM (SELECT distinct r.stockist_id AS return_stockist_id, r.claim_number, r.status, r.received_date,  CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END AS days_taken, r.lr_booking_date, r.manufacturer FROM tbl_returns r LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer left join tbl_manufacturer mr on mr.manufacturer_id= r.manufacturer and r.tenant_id=tm.tenant_id  WHERE r.tenant_id =:tenantId AND r.status != 'PENDING'   and tm.status='ACTIVE' and mr.status='ACTIVE' AND EXTRACT(MONTH FROM  r.received_date) =:month and EXTRACT(YEAR FROM r.received_date) = EXTRACT(YEAR FROM CURRENT_DATE)  AND CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END > tm.deadline_claim ) AS tableA INNER JOIN (SELECT distinct a.stockist_id AS stockist_id, a.stockist_name, a.city_id, c.city_name, b.sap_id,b.location,b.manufacture FROM tbl_stockist a LEFT JOIN tbl_stockist_manufacture b ON a.stockist_id = b.stockist_id and a.tenant_id=b.tenant_id  LEFT JOIN tbl_manager_manufacturer_mapping mp ON mp.manufacturerid = b.manufacture LEFT JOIN tbl_city c ON a.city_id = c.city_code WHERE a.tenant_id = :tenantId AND mp.user_id = :userId) AS tableB ON tableA.return_stockist_id = tableB.stockist_id and tableA.manufacturer=tableB.manufacture ", nativeQuery = true) 
    List<Object[]> claimsNotClosedMonth(@Param("tenantId") String tenantId,@Param("userId") String userId,int month);
   
    
    @Query(value = "SELECT * FROM ( SELECT DISTINCT r.stockist_id AS return_stockist_id, r.claim_number,r.status,r.received_date, CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END AS daysTaken, r.lr_booking_date, r.manufacturer FROM tbl_returns r LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer LEFT JOIN tbl_manufacturer mr ON mr.manufacturer_id = r.manufacturer AND r.tenant_id = tm.tenant_id WHERE r.tenant_id =:tenantId AND r.status != 'PENDING' AND tm.status = 'ACTIVE' AND mr.status = 'ACTIVE'   and EXTRACT(MONTH FROM  r.received_date) =:month and EXTRACT(YEAR FROM r.received_date) = EXTRACT(YEAR FROM CURRENT_DATE)  AND CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END > tm.deadline_claim ) AS tableA INNER JOIN ( SELECT DISTINCT a.stockist_id AS stockist_id, a.stockist_name, a.city_id, c.city_name, b.sap_id, b.location, b.manufacture FROM tbl_stockist a LEFT JOIN tbl_stockist_manufacture b ON a.stockist_id = b.stockist_id AND a.tenant_id = b.tenant_id LEFT JOIN tbl_city c ON a.city_id = c.city_code WHERE a.tenant_id =:tenantId ) AS tableB ON tableA.return_stockist_id = tableB.stockist_id AND tableA.manufacturer = tableB.manufacture  ", nativeQuery = true)
    List<Object[]> claimsNotClosedSuperMonth(@Param("tenantId") String tenantId,int month);
    
   @Query(value = "SELECT * FROM ( SELECT DISTINCT r.stockist_id AS return_stockist_id, r.claim_number,r.status,r.received_date, CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END AS daysTaken, r.lr_booking_date, r.manufacturer FROM tbl_returns r LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer LEFT JOIN tbl_manufacturer mr ON mr.manufacturer_id = r.manufacturer AND r.tenant_id = tm.tenant_id WHERE r.tenant_id =:tenantId AND r.status != 'PENDING' AND tm.status = 'ACTIVE' AND mr.status = 'ACTIVE'    AND CASE WHEN r.status = 'CN_CREATED' THEN ROUND((r.modified_on - r.received_date)) ELSE ROUND((CURRENT_DATE - r.received_date)) END > tm.deadline_claim ) AS tableA INNER JOIN ( SELECT DISTINCT a.stockist_id AS stockist_id, a.stockist_name, a.city_id, c.city_name, b.sap_id, b.location, b.manufacture FROM tbl_stockist a LEFT JOIN tbl_stockist_manufacture b ON a.stockist_id = b.stockist_id AND a.tenant_id = b.tenant_id LEFT JOIN tbl_city c ON a.city_id = c.city_code WHERE a.tenant_id =:tenantId ) AS tableB ON tableA.return_stockist_id = tableB.stockist_id AND tableA.manufacturer = tableB.manufacture  ", nativeQuery = true)
    List<Object[]> claimsNotClosedSuperMonth(@Param("tenantId") String tenantId);
    
    
    
    @Query(value = "SELECT r.claim_number, r.status, r.received_date, s.stockist_name, " +
            "EXTRACT(DAY FROM AGE(CURRENT_DATE, r.received_date)) AS days_taken ,mr.manufacturer_name " +
            "FROM tbl_returns r  " +
            "LEFT JOIN tbl_stockist s ON r.stockist_id = s.stockist_id left join tbl_manager_manufacturer_mapping m on r.manufacturer=m.manufacturerid left join tbl_manufacturer mr on m.manufacturerid =mr.manufacturer_id left join tbl_tenant_manufacture tm on mr.manufacturer_id =tm.manufacturer_id   " +
            "WHERE r.tenant_id = :tenantId and m.user_id =:userId AND mr.status = 'ACTIVE' and tm.status='ACTIVE'" +
            "AND r.created_on >= :startDate AND r.created_on <= :endDate  and (r.received_date + tm.recived_not_checked) < :currentDate " +
            "AND r.status = 'RECEIVED'", nativeQuery = true)
    List<Object[]> receivedNotChecked(@Param("tenantId") String tenantId,@Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate, @Param("currentDate") Date currentDate);
    
      
    
    @Query(value="SELECT distinct r.claim_number, n.created_on, s.stockist_name, t.transport_name,t.location, EXTRACT(DAY FROM AGE(CURRENT_DATE, n.created_on)) AS days,mr.manufacturer_name " +
            "FROM tbl_returns r " +
            "LEFT JOIN tbl_stockist s ON r.stockist_id = s.stockist_id left join tbl_manager_manufacturer_mapping m on r.manufacturer=m.manufacturerid left join tbl_manufacturer mr on m.manufacturerid =mr.manufacturer_id left join tbl_tenant_manufacture tm on mr.manufacturer_id =tm.manufacturer_id   " +
            "LEFT JOIN tbl_return_note n ON r.return_id  = n.return_id "
            + "left join tbl_transport t on t.transport_id=r.transporter_id " +
            "WHERE r.tenant_id =:tenantId and m.user_id =:userId " +
            "AND r.created_on >= :startDate AND r.created_on <= :endDate and (n.created_on + tm.grrn_not_second_cheque) < :currentDate " +
            "AND r.status = 'GRRN_CREATED' AND n.note_type = 'GRRN' AND tm.second_check='true'", nativeQuery=true)
List<Object[]> secondCheckNotCompleted(@Param("tenantId") String tenantId,@Param("userId") String userId, 
                                       @Param("startDate")  LocalDateTime startDate, 
                                       @Param("endDate")  LocalDateTime endDate,@Param("currentDate") Date currentDate);

	@Query(value = "select * from tbl_returns where return_number =:returnNumber AND  tenant_id =:tenantId ",nativeQuery = true)
	public Optional<Return> getByReturnNumber(String returnNumber , String tenantId);
	
	@Query(value = "select * from tbl_returns where serial_number =:serialNumber AND  tenant_id =:tenantId ",nativeQuery = true)
	public Optional<Return> getBySerialNumber(String serialNumber , String tenantId);
	
	
	
    
   
	
	
	
}
