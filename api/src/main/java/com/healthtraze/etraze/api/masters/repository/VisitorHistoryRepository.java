package com.healthtraze.etraze.api.masters.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.VisitorHistory;


@Repository
public interface VisitorHistoryRepository extends BaseRepository<VisitorHistory, String>{

	@Query(value="select * from tbl_visitor_history where phone =:phone and in_time =:inTime",nativeQuery = true)
	public Optional<VisitorHistory> getByPhoneAndIntime(String phone, Date inTime);
	
	@Query(value="SELECT * FROM tbl_visitor_history WHERE visitor_id =:id AND (:mon IS NULL OR :mon = 0 OR month = :mon) ",nativeQuery = true)
	public Page<VisitorHistory> getAllById(String id, int mon, Pageable paging);
	

}
