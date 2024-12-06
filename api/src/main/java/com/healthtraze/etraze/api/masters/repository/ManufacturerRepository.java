package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.constant.QueryConstants;
import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.dashboard.controller.DashBoardDTO;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;


@Repository
public interface ManufacturerRepository extends BaseRepository<Manufacturer, String> {

    List<Manufacturer> findByTenantId(String tenantId);
     
    

    @Query(value = " SELECT * FROM tbl_manufacturer where (manufacturer_id ILIKE %:value% OR manufacturer_name ILIKE %:value% OR short_name ILIKE %:value% OR status ILIKE %:value% )  ", nativeQuery = true)
    public List<Manufacturer> findManufactureByCount(String value);
    
    @Query(value = " SELECT * FROM tbl_manufacturer where (manufacturer_id ILIKE %:value% OR manufacturer_name ILIKE %:value% OR short_name ILIKE %:value% OR status ILIKE %:value% )  ", nativeQuery = true)
    public List<Manufacturer> findManufactureByCount(String value, Pageable paging);
    
    @Query(value = "SELECT short_name FROM tbl_manufacturer where manufacturer_id=:manufacturerId", nativeQuery = true)
    public String findManufacturName(String manufacturerId);


    List<Manufacturer> findByManufacturerNameAndTenantId(String manufacturerName, String orgId);


    @Query(value = "SELECT * FROM tbl_manufacturer Where (LOWER( manufacturer_name)= LOWER(:manufacturerName) or LOWER ( manufacturer_id)=LOWER(:manufacturerName))", nativeQuery = true)
    List<Manufacturer> findByManufacturerName(String manufacturerName);//
    
    @Query(value = "SELECT * FROM tbl_manufacturer Where (LOWER(REPLACE(manufacturer_name,' ','')) = :manufacturerName OR LOWER(REPLACE(manufacturer_name,' ','')) = :manufacturerName) AND status='ACTIVE' LIMIT 1", nativeQuery = true)
    Optional<Manufacturer> findByManufacturerNameAndStatus(String manufacturerName);

    @Query(value = "SELECT * FROM tbl_tenant_manufacturer WHERE tenant_id = :tenantId", nativeQuery = true)
    public List<Manufacturer> findManufactureByTenantId(@Param("tenantId") String tenantId);


    @Query(value = "SELECT * FROM tbl_manufacturer a inner JOIN tbl_tenant_manufacture b ON a.manufacturer_id = b.manufacturer_id where b.tenant_id = :tenantId And a.status='ACTIVE' AND b.status='ACTIVE' order by a.manufacturer_name ASC", nativeQuery = true)
    public List<Manufacturer> findManufactureByTenant(@Param("tenantId") String tenantId);


    @Query(value = "SELECT MAX(CAST(SUBSTRING(manufacturer_id FROM LENGTH('MF')+1) AS INTEGER)) AS lastSequence FROM tbl_manufacturer", nativeQuery = true)
    public Integer getLastSequence();

    @Query(value = "SELECT * FROM tbl_manufacturer where manufacturer_Id in :manfList and status ='ACTIVE'", nativeQuery = true)
    public List<Manufacturer> findActiveManufacturers(@Param("manfList") List<String> manfList);


    @Query(value = "SELECT * FROM tbl_manufacturer WHERE manufacturer_id in :manfList", nativeQuery = true)
    List<Manufacturer> findManufacturersList(List<String> manfList);

    @Query(value = "SELECT * FROM tbl_manufacturer where status='ACTIVE' order by manufacturer_name ASC", nativeQuery = true)
    List<Manufacturer> findManufactureByTenant();

    @Query(value = QueryConstants.DASHBOARD_QUERY, nativeQuery = true)
    public List<DashBoardDTO> findDashBoardData(String tenantId);

    @Query(value = "SELECT COUNT(DISTINCT CASE WHEN t.type = 'order' THEN t.ticket_id END) AS total,SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS assignedCount,COUNT(DISTINCT t.ticket_id) - SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS unassignedCount FROM tbl_ticket t LEFT JOIN    tbl_ticket_order_invoice toi ON t.ticket_id = toi.ticket_id WHERE t.assigned_to =:assignedTo AND t.tenant_id=:tenantId  UNION ALL SELECT COUNT(DISTINCT CASE WHEN t.type != 'order' THEN t.ticket_id END) AS total,SUM(CASE WHEN t.status = 'INPROCESS' THEN 1 ELSE 0 END) AS assignedCount,SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) AS unassignedCount FROM tbl_ticket t WHERE t.assigned_to =:assignedTo AND t.tenant_id=:tenantId", nativeQuery = true)
    public List<DashBoardDTO> findDashBoardDataUser(String tenantId, String assignedTo);

    
    @Query(value ="SELECT COALESCE(( select count(*) from tbl_tenant where tenant_id=:tenantId),0) AS tenant, COALESCE(SUM(CASE WHEN u.role_name != 'STOCKIST' AND u.role_name !='TRANSPORT' THEN 1 ELSE 0 END), 0) AS users, COALESCE(SUM(CASE WHEN u.role_name ='STOCKIST' THEN 1 ELSE 0 END), 0) AS stockist, COALESCE(SUM(CASE WHEN u.role_name ='TRANSPORT' THEN 1 ELSE 0 END), 0) AS transporter, COALESCE((SELECT COUNT(*) FROM tbl_manufacturer m LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = m.manufacturer_id WHERE tm.tenant_id =:tenantId and m.status='ACTIVE' and tm.status='ACTIVE'), 0) AS manufacturer, COALESCE((SELECT COUNT(*) FROM tbl_ticket t LEFT JOIN tbl_ticket_order_invoice inv ON inv.ticket_id = t.ticket_id WHERE t.type ='order' AND t.tenant_id =:tenantId), 0)  AS ordercount, COALESCE(( select count(*) from tbl_returns where tenant_id=:tenantId),0) as returns FROM tbl_user u WHERE u.tenant_id =:tenantId " ,nativeQuery = true)
    public List<Object[]> finddashboardCountTenantId(@Param(value="tenantId") String tenantId);
        
    @Query(value ="SELECT COALESCE(( select count(*) from tbl_tenant ),0) AS tenant, COALESCE(SUM(CASE WHEN u.role_name != 'STOCKIST' AND u.role_name !='TRANSPORT' THEN 1 ELSE 0 END), 0) AS users, COALESCE(SUM(CASE WHEN u.role_name ='STOCKIST' THEN 1 ELSE 0 END), 0) AS stockist, COALESCE(SUM(CASE WHEN u.role_name ='TRANSPORT' THEN 1 ELSE 0 END), 0) AS transporter, COALESCE((SELECT COUNT(*) FROM tbl_manufacturer m  WHERE  m.status='ACTIVE' ), 0) AS manufacturer, COALESCE((SELECT COUNT(*) FROM tbl_ticket t LEFT JOIN tbl_ticket_order_invoice inv ON inv.ticket_id = t.ticket_id WHERE t.type ='order' ), 0) AS ordercount, COALESCE((select count(*) from tbl_returns ),0) as returns FROM tbl_user u  " ,nativeQuery = true)
    public List<Object[]> finddashboardCount();
    
    

  
    @Query(value = "SELECT * FROM tbl_manufacturer Where (LOWER( manufacturer_name)= LOWER(:name) or LOWER ( manufacturer_id)=LOWER(:name))", nativeQuery = true)
    public List<Manufacturer> findManufactureByName(String name);

    

    @Query(value ="SELECT COALESCE(SUM(CASE WHEN r.type != 'order' OR r.type IS NULL THEN 1 ELSE 0 END), 0) AS totaltickets, COALESCE(SUM(CASE WHEN (r.status != 'COMPLETED' AND (r.type != 'order' OR r.type IS NULL)) AND EXTRACT(DAY FROM AGE(CURRENT_DATE, r.created_on)) > tm.deadline_ticket THEN 1 ELSE 0 END), 0) AS TicketDeadlines, COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) AS Completed, COALESCE(SUM(CASE WHEN r.type = 'order' THEN 1 ELSE 0 END), 0) AS TotalOrder, COALESCE(SUM(CASE WHEN inv.status IN ('DELIVERED', 'POD RECEIVED') THEN 1 ELSE 0 END), 0) AS CompletedOrder, COALESCE(SUM(CASE WHEN r.type = 'order' AND EXTRACT(DAY FROM AGE(CURRENT_DATE, TO_DATE(r.assigned_on, 'yyyy-mm-dd'))) > tm.deadline_order THEN 1 ELSE 0 END), 0) AS overdueorderdeadlines,COALESCE(SUM(CASE WHEN r.status='INPROCESS'  THEN 1 ELSE 0 END),0) FROM tbl_ticket r LEFT JOIN tbl_ticket_order_invoice inv ON inv.ticket_id = r.ticket_id LEFT JOIN tbl_manufacturer m ON m.manufacturer_id = r.manufacturer_id LEFT JOIN tbl_manager_manufacturer_mapping mp ON mp.manufacturerid = m.manufacturer_id LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = m.manufacturer_id AND tm.tenant_id = r.tenant_id WHERE r.tenant_id =:tenantId AND mp.user_id =:userId AND m.status = 'ACTIVE' AND tm.status = 'ACTIVE' ", nativeQuery = true)
    List<Object[]> findTicketManagerCount(@Param("tenantId")String tenantId, @Param("userId")String userId);

    
    
    
    @Query(value = "SELECT " +
            "    COALESCE(SUM(CASE WHEN r.status ='RECEIVED' THEN 1 ELSE 0 END), 0) AS received, " +
            "    COALESCE(SUM(CASE WHEN r.status ='CN_CREATED' THEN 1 ELSE 0 END), 0) AS cn, " +
            "    COALESCE(SUM(CASE WHEN r.status IN ('PENDING', 'RECEIVED', 'CHECKED', 'CHECKED II', 'GRRN_CREATED', 'CN_CREATED') THEN 1 ELSE 0 END), 0) AS total, " +
            "    COALESCE(SUM(CASE WHEN r.status IN ('RECEIVED', 'CHECKED', 'CHECKED II', 'GRRN_CREATED') AND  EXTRACT(DAY FROM AGE(CURRENT_DATE, r.received_date)) > tm.deadline_claim THEN 1 ELSE 0 END), 0) AS overdueclaim " +
            "FROM " +
            "    tbl_returns r " +
            "LEFT JOIN " +
            "    tbl_manager_manufacturer_mapping mm ON r.manufacturer = mm.manufacturerId " +
            "LEFT JOIN " +
            "    tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer AND tm.tenant_id = r.tenant_id " +
            "WHERE " +
            "    r.tenant_id = :tenantId " +
            "    AND mm.user_id = :userId", nativeQuery = true)
     public List<Object[]> getReturnsCountByManager(@Param("tenantId") String tenantId, @Param("userId") String userId);
    
         @Query(value = "SELECT COALESCE(SUM(CASE WHEN inv.status IN ('DISPATCHED') THEN 1 ELSE 0 END), 0) AS dispatched, COALESCE(SUM(CASE WHEN inv.status IN ('DELIVERED', 'POD RECEIVED') THEN 1 ELSE 0 END), 0) AS delivered  FROM tbl_ticket r LEFT JOIN tbl_ticket_order_invoice inv ON inv.ticket_id = r.ticket_id LEFT JOIN tbl_manufacturer m ON m.manufacturer_id = r.manufacturer_id LEFT JOIN tbl_manager_manufacturer_mapping mp ON mp.manufacturerid = r.manufacturer_id LEFT JOIN tbl_tenant_manufacture tm ON tm.manufacturer_id = r.manufacturer_id AND tm.tenant_id = r.tenant_id   join tbl_ticket_status_history ts ON ts.invoice = inv.invoice_number AND ts.status = 'DISPATCHED'   LEFT JOIN tbl_stockist s ON s.stockist_id = r.stockist_id LEFT JOIN tbl_stockist_manufacture sm ON sm.manufacture = r.manufacturer_id AND r.stockist_id = sm.stockist_id WHERE r.tenant_id =:tenantId AND mp.user_id =:userId AND m.status = 'ACTIVE' and ts.status = 'DISPATCHED' AND tm.status = 'ACTIVE' ", nativeQuery = true)
    	List<Object[]> findDeliveryCounts(@Param("tenantId") String tenantId, @Param("userId") String userId);

    
    
    
    @Query(value = "SELECT  COALESCE(user_count, 0) AS user_count, COALESCE(manager_count, 0) AS manager_count, COALESCE(stockist_count, 0) AS stockist_count, COALESCE(transport_count, 0) AS transport_count FROM (SELECT COUNT(DISTINCT CASE WHEN u.role_name ='USER' THEN u.user_id END) AS user_count, COUNT(DISTINCT CASE WHEN u.role_name = 'MANAGER' THEN u.user_id END) AS manager_count, COUNT(DISTINCT CASE WHEN t.tenant_id =:tenantId THEN t.transport_id END) AS transport_count, COUNT(DISTINCT CASE WHEN s.tenant_id =:tenantId THEN s.stockist_id END) AS  stockist_count  FROM  tbl_user u LEFT JOIN tbl_transport t ON t.tenant_id = u.tenant_id  LEFT JOIN tbl_stockist s ON s.tenant_id = u.tenant_id WHERE  u.tenant_id =:tenantId) AS counts", nativeQuery = true)
    List<Object[]> findTenantDetailsCount(@Param("tenantId") String tenantId);

    @Query(value = "SELECT * FROM tbl_manufacturer Where (LOWER(REPLACE(manufacturer_name,' ','')) = :manufacturerName AND status='ACTIVE' ", nativeQuery = true)
    Optional<Manufacturer> findByManufacturerNameAnd(String manufacturerName);
    
    
    
    
}




