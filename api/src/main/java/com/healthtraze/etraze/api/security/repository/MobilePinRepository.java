package com.healthtraze.etraze.api.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.MobilePin;

@Repository
public interface MobilePinRepository extends BaseRepository<MobilePin, Long>  {
	
	@Query(value= "select * from tbl_mobile_pin where pin=:pin and tenant_id=:tenantId", nativeQuery=true)
	public Optional<MobilePin> loginByPin(@Param("pin") String pin ,@Param("tenantId") String tenantId);
	
	@Query(value= "select * from tbl_mobile_pin where user_id=:userId and tenant_id=:tenantId ", nativeQuery=true)
	public Optional<MobilePin> findByUserId(String userId ,  String tenantId);

	@Query(value= "select * from tbl_mobile_pin where user_id=:userId and tenant_id=:tenantId", nativeQuery=true)
	public MobilePin updatePin( String userId ,  String tenantId);
}
