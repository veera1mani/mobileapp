package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ChequeStatusHistory;

@Repository
public interface ChequeStatusHistoryRepository extends BaseRepository<ChequeStatusHistory,String> {
	
	@Query(value="SELECT * FROM tbl_cheque_status_history WHERE cheque_id=:id and tenant_id=:tenantId ORDER BY id ASC", nativeQuery = true)
	public List<ChequeStatusHistory> findchequeStatusHistory(Long id,@Param ("tenantId") String tenantId);
	
	@Query(value="SELECT * FROM tbl_cheque_status_history WHERE cheque_number=:chequeNumber and tenant_id=:tenantId ORDER BY id ASC", nativeQuery = true)
	public List<ChequeStatusHistory> findchequeStatusHistory(String chequeNumber,@Param ("tenantId") String tenantId);

}
