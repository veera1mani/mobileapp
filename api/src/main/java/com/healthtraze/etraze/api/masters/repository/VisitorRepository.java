package com.healthtraze.etraze.api.masters.repository;

 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.Visitor;


@Repository
public interface VisitorRepository extends BaseRepository<Visitor, String>{

	
	public Optional<Visitor> findByPhone(String phone);
	//DATE(in_time) = CURRENT_DATE and
	@Query(value="select * from tbl_visitors where tenant_id =:tenantId AND out_time IS NULL", nativeQuery = true)
	public List<Visitor> findByTenantAndToday(String tenantId);
	
	@Query(value="select * from tbl_visitors v where tenant_id =:tenantId and (v.name ILIKE %:search% OR v.email_id ILIKE %:search% OR v.phone ILIKE %:search%)", nativeQuery = true)
	public Page<Visitor> findByTenantAndSearch(String search,String tenantId, Pageable paging);
	
	@Query(value="SELECT v.* FROM tbl_visitors v LEFT JOIN tbl_visitor_history h ON v.id = h.visitor_id WHERE h.tenant_id =:tenantId AND h.month =:mon and (v.name ILIKE %:search% OR v.email_id ILIKE %:search% OR v.phone ILIKE %:search%) GROUP BY v.id", nativeQuery = true)
	public Page<Visitor> findByTenantMonthSearch(String search,String tenantId, int mon , Pageable paging);
	
	@Query(value="select * from tbl_visitors where in_time is not null and out_time is null", nativeQuery = true)
	public List<Visitor> findAllNotOut();
	

}