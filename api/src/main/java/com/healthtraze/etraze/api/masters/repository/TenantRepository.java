package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.base.repository.BaseRepository;
@Repository
public interface TenantRepository extends BaseRepository<Tenant,String>{

	
	public Optional<Tenant> findByTenantName(String tenantName);
	
	@Query(value = "select * from tbl_tenant where tenant_id=:tenantId",nativeQuery = true)
	public Optional<Tenant> findByEmail(@Param("tenantId") String tenantId);
	
	@Query(value = "select * from tbl_tenant where tenant_code= :tenantIdPrefix",nativeQuery = true)
	public Optional<Tenant> findByTenantCode(String tenantIdPrefix);
	
	@Query(value = "select * from tbl_tenant where (tenant_name ILIKE %:search% OR first_name ILIKE %:search% OR email_id ILIKE %:search% OR phone_number ILIKE %:search% OR status ILIKE %:search%)",nativeQuery = true)
	public Page<Tenant> findAll(String search,Pageable pageable);
	
	@Query(value = "select multi_qr from tbl_tenant where tenant_id=:tenantId",nativeQuery = true)
	public boolean findQrType(@Param("tenantId") String tenantId);
	
	@Query(value = "select * from tbl_tenant where status='APPROVED'",nativeQuery = true)
	public List<Tenant> findApprovedTenants();
	
}
