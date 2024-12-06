package com.healthtraze.etraze.api.security.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.SendMail;

@Repository
public interface EmailRepository extends BaseRepository<SendMail, String>{
	List<SendMail> findByStatus(String status);
	
	@Query(value="SELECT * FROM tbl_send_mail WHERE tenant_id = :tenantId AND (to_name ILIKE %:search% OR subject ILIKE %:search% OR (TO_CHAR(deliver_date, 'DD-Mon-YYYY') ILIKE %:search% OR TO_CHAR(deliver_date, 'DD-MM-YYYY') ILIKE %:search%)) order by created_on desc", nativeQuery = true)
	Page<SendMail> findByTenantId(@Param("tenantId") String tenantId, String search, Pageable pageable);


}
