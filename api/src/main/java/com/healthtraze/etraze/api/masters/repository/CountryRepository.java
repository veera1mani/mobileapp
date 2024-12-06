package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.documents.Country;

@Repository
public interface CountryRepository extends BaseRepository<Country, String> {


    Optional<Country> findByCountryCode(String countryCode);

    List<Country> findByTenantId(String tenantId);

	// List<Country> findByCountryName(String countryName);
	@Query(value = "SELECT * FROM tbl_country WHERE country_code ILIKE %:value% OR country_name ILIKE %:value% OR status ILIKE %:value%", nativeQuery = true)
	List<Country> findAllCountry(@Param("value") String value, Pageable pageable);

    @Query(value = "SELECT * from tbl_country", nativeQuery = true)
    List<Country> findAllCount();

	Optional<Country> findByCountryName(String countryName);
	
	
	   @Query(value="SELECT * FROM tbl_country WHERE (LOWER(REPLACE(country_name,' ','')) = :countryName OR LOWER(REPLACE(country_code,' ','')) = :countryName)",nativeQuery =true)
	   List<Country> findAllByCountryName(@Param("countryName") String countryName);
	   
	   @Query(value="SELECT * FROM tbl_country WHERE (LOWER(REPLACE(country_name,' ','')) = :countryName OR LOWER(REPLACE(country_code,' ','')) = :countryName) AND status='ACTIVE' LIMIT 1",nativeQuery =true)
	   Optional<Country> findByCountryNameOrCode(@Param("countryName") String countryName);
	   
	   @Query(value="SELECT * FROM tbl_country WHERE (LOWER(REPLACE(country_name,' ','')) = :countryName) AND status='ACTIVE' ",nativeQuery =true)
	   Optional<Country> findCountryName(@Param("countryName") String countryName);
	   
	   @Query(value="SELECT * FROM tbl_country WHERE (LOWER(REPLACE(country_name,' ','')) = :countryName OR LOWER(REPLACE(country_code,' ','')) = :code)",nativeQuery =true)
	   Optional<Country> findByCountryName(@Param("countryName") String countryName,@Param("code") String code);
	
	
	@Query(value="SELECT * FROM tbl_country where status='ACTIVE'  order by country_name ASC",nativeQuery =true)
	List<Country> findAllByCountry();

    
       


}