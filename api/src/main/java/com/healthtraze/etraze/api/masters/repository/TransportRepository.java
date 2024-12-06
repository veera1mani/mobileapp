package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.Transport;



@Repository
public interface TransportRepository extends JpaRepository<Transport, String> {

	List<Transport> findAllByStatus(String status);
	
    
	public Optional<Transport> findByFirstName(String transportName);
	@Query(value ="SELECT COALESCE(MAX(CAST(SUBSTRING(transport_id FROM LENGTH(:pathId) + 1) AS INTEGER)), 0) AS lastSequence FROM tbl_transport WHERE tenant_id = :tenantId",nativeQuery = true)
	Integer getLastSequence(String pathId,String tenantId);
	
	@Query(value="SELECT * FROM tbl_transport t LEFT JOIN tbl_city ct ON t.location = ct.city_code WHERE t.tenant_id=:tenantId AND isenable=true AND "
			+ "(t.first_name ILIKE %:value% OR "
			+ "t.last_name ILIKE %:value% OR "
			+ "t.mobile ILIKE %:value% OR "
			+ "t.transport_name ILIKE %:value% OR " 
			+ "t.email ILIKE %:value% OR "
			+ "ct.city_name ILIKE %:value% OR "
			+ "t.transport_id ILIKE %:value%)",nativeQuery = true)
public List<Transport> findByTenantId(Pageable pageable,@Param(value = "tenantId") String id, @Param(value = "value") String value);

	
	@Query(value="select * from tbl_transport t LEFT JOIN tbl_city ct ON t.location = ct.city_code WHERE t.tenant_id=:tenantId AND isenable=true AND "
			+ "(t.first_name ILIKE %:value% OR "
			+ "t.last_name ILIKE %:value% OR "
			+ "t.mobile ILIKE %:value% OR "
			+ "t.transport_name ILIKE %:value% OR "
			+ "t.email ILIKE %:value% OR "
			+ "ct.city_name ILIKE %:value% OR "
			+ "t.transport_id ILIKE %:value%)",nativeQuery = true)
	public List<Transport> findByTenantId(@Param(value = "tenantId") String id, String value);
	
	
	
	@Query(value="select * from tbl_transport where tenant_id=:tenantId AND isenable=true AND status='ACTIVE' ORDER BY transport_name ASC",nativeQuery = true)
	public List<Transport> findByTenantId(@Param(value = "tenantId") String id);
	
	@Query(value="select t.transport_name from tbl_transport t where t.tenant_id=:tenantId AND t.isenable=true AND t.status='ACTIVE' ORDER BY transport_name ASC",nativeQuery = true)
	public List<String> findByTenantIdForName(@Param(value = "tenantId") String id);
	
	@Query(value="select * from tbl_transport where tenant_id=:tenantId AND isenable=true AND email=:mail",nativeQuery = true)
	public Optional<Transport> findByTenantIdAndTransporter(@Param(value = "tenantId") String tenantId,@Param(value = "mail") String mail);
	
	@Query(value="SELECT COALESCE(SUM(CASE WHEN t.status='DISPATCHED' OR t.status='DELIVERED' THEN 1 ELSE 0 END), 0) AS Total,COALESCE(SUM(CASE WHEN t.status='DISPATCHED' THEN 1 ELSE 0 END), 0) AS Dispached,COALESCE(SUM(CASE WHEN t.status='DELIVERED' THEN 1 ELSE 0 END), 0) AS Delivired from tbl_ticket t LEFT JOIN tbl_ticket_order o ON t.ticket_id = o.ticket_id WHERE t.type='order'AND t.tenant_id=:tenantId  AND o.transporter=:transportId",nativeQuery = true)
	public Object[] findByTenantIdAndTransporterOrderCount(@Param(value = "tenantId") String tenantId,@Param(value = "transportId") String transportId);
		@Query(value = "SELECT * FROM tbl_ticket t LEFT JOIN tbl_ticket_order AS to_alias ON t.ticket_id = to_alias.ticket_id WHERE t.type='order' AND t.tenant_id=:tenantId AND to_alias.transporter=:transportId", nativeQuery = true)
	public List<Ticket> findTransporterOrders(@Param(value = "tenantId") String tenantId, @Param(value = "transportId") String transportId);

	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tbl_transport SET status=:status WHERE transport_id=:transport_id and tenant_id=:tenantId",nativeQuery = true)
	void setStatus(@Param("transport_id") String transportId,@Param ("tenantId") String tenantId,@Param("status") String status);
	
	
	
	@Query(value = "SELECT * FROM tbl_transport Where (LOWER(REPLACE(transport_name,' ','')) = :transport) AND status='ACTIVE' AND isenable=true and tenant_id =:tenantId LIMIT 1", nativeQuery = true)
    Optional<Transport> findByTransporterNameAndStatus(String transport , String tenantId);


	

}
