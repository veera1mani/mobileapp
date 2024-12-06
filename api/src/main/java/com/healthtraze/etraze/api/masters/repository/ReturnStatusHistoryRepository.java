package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ReturnStatusHistory;


@Repository
public interface ReturnStatusHistoryRepository extends BaseRepository<ReturnStatusHistory, String>{

	
	public List<ReturnStatusHistory> findBySerialNumber(String serialNumber);
	
	@Query(value = "select * from tbl_return_status_history where return_id =:returnId AND tenant_id =:tenantId Order by id ASC",nativeQuery = true) 
	public List<ReturnStatusHistory> findByReturnId(String returnId , String tenantId);
	
	@Query(value = "select * from tbl_return_status_history where return_id =:returnId  ",nativeQuery = true) 
	public Optional<ReturnStatusHistory> findByReturnIdForReceived(String returnId);


}
