package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.Deadlines;

@Repository
public interface DeadlinesRepository extends BaseRepository<Deadlines,Long> {
	
	 @Query(value="select * from tbl_deadlines where tenant_id=:tenatId ",nativeQuery =true)
	 List<Deadlines> findAll(@Param("tenatId") String tenatId);
	 
	 @Query(value="select * from tbl_deadlines where tenant_id=:tenatId AND user_id =:userId ",nativeQuery =true)
	 Optional<Deadlines> findByTenantId(@Param("tenatId") String tenatId,@Param("userId") String userId);
	 
	 @Query(value="select * from tbl_deadlines where tenant_id=:tenatId AND user_id =:userId ",nativeQuery =true)
	 List<Deadlines> findByAllTenantId(@Param("tenatId") String tenatId,@Param("userId") String userId);

}
