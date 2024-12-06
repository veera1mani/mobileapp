package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.documents.State;

@Repository
public interface StateRepository extends  BaseRepository<State, String> {
    
    
    List<State> findAllByStateCode( String stateCode);
    
    

    Optional<State> findAllByStateName(String stateName);
    


    List<State> findByStateNameAndStateCodeAndCountryCode(String stateName, String stateCode, String countryCode);



    List<State> findByTenantId(String tenantId);

	@Query(value = "SELECT * FROM tbl_state WHERE state_code ILIKE %:value% OR state_name ILIKE %:value% OR status ILIKE %:value% OR country_code ILIKE %:value%", nativeQuery = true)
	List<State> findAllState(String value, Pageable paging);
	
	@Query(value = "SELECT * FROM tbl_state WHERE state_code ILIKE %:value% OR state_name ILIKE %:value% OR status ILIKE %:value%  OR country_code ILIKE %:value% ", nativeQuery = true)
	List<State> findAllState(String value);
	
     
    @Query(value="SELECT * FROM tbl_state ",nativeQuery =true)
    List<State> findAllCount();



    @Query(value = "SELECT * FROM tbl_state WHERE LOWER(REPLACE(state_name,' ','')) = :stateName  or LOWER(REPLACE(state_code,' ','')) = :stateName ", nativeQuery = true)
    List<State> findByStateName( @Param("stateName") String stateName);
    
    @Query(value = "SELECT * FROM tbl_state WHERE LOWER(REPLACE(state_name,' ','')) = :stateName  or LOWER(REPLACE(state_code,' ','')) = :stateName AND status='ACTIVE'", nativeQuery = true)
    List<State> findByStateNameWithStatus( @Param("stateName") String stateName);
	
	@Query(value = "SELECT * FROM tbl_state WHERE( LOWER(REPLACE(state_name,' ','')) = :stateName  or LOWER(REPLACE(state_code,' ','')) = :stateName)AND country_code=:countryCode  AND status='ACTIVE' LIMIT 1", nativeQuery = true)
    Optional<State> findByStateNameWithStatusAndCountry(@Param("stateName") String stateName,@Param("countryCode") String countryCode);
	
	@Query(value = "SELECT * FROM tbl_state WHERE LOWER(REPLACE(state_name,' ','')) = :stateName AND status='ACTIVE'", nativeQuery = true)
    Optional<State> findStateName(@Param("stateName") String stateName);
    
    @Query(value = "SELECT * FROM tbl_state WHERE LOWER(REPLACE(state_name,' ','')) = :stateName OR LOWER(REPLACE(state_code,' ','')) = :stateCode", nativeQuery = true)
    Optional<State> findStateByName(@Param("stateName") String stateName,@Param("stateCode") String stateCode);
    
    @Query(value="SELECT * FROM tbl_state where status='ACTIVE' AND country_code=:countryCode order by state_name ASC",nativeQuery =true)
    List<State> findAllByCountryCode(String countryCode);

    
    

}