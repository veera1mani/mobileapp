package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.documents.City;

@Repository
public interface CityRepository extends  BaseRepository<City, String> {
        

    Optional<City> findAllByCountryCode(String countryCode);
    
    Optional<City> findAllByCityCode(String cityCode);
    
    Optional<City> findAllByCityName(String cityName);
    
    
    @Query("select rs from City rs where rs.countryCode =:countryCode and rs.stateCode =:stateCode ")
    Optional<City> findAllByCode(@Param(Constants.COUNTRYCODE) String countryCode,@Param(Constants.STATECODE) String stateCode);


    List<City> findByCountryCodeAndStateCodeAndCityCodeAndCityName(String countryCode, String stateCode,
            String cityCode, String cityName);
 
    List<City> findByTenantId(String tenantId);


    

	@Query(value = "SELECT distinct c.*,s.* FROM tbl_city c left join tbl_state s on c.state_code =s.state_code  WHERE c.city_code ILIKE %:value% OR c.city_name ILIKE %:value% OR s.state_name ILIKE %:value% OR c.status ILIKE %:value% OR c.country_code ILIKE %:value%", nativeQuery = true)
	List<City> findAllCity(String value, Pageable paging);
	
	@Query(value = "SELECT distinct c.*,s.* FROM tbl_city c left join tbl_state s on c.state_code =s.state_code  WHERE c.city_code ILIKE %:value% OR c.city_name ILIKE %:value% OR s.state_name ILIKE %:value% OR c.status ILIKE %:value% OR c.country_code ILIKE %:value%", nativeQuery = true)
	List<City> findAllCity(String value);

    @Query(value="select * from tbl_city ",nativeQuery =true)
    List<City> findAllCount();
  
    @Query(value="select * from tbl_city where status='ACTIVE' order by city_name ASC",nativeQuery =true)
    List<City> findAllCitis();
    
    @Query(value="select * from tbl_city where city_code=:cityCode AND state_code=:stateCode",nativeQuery =true)
    Optional<City> findByCityCodeAndStateCode(String cityCode,String stateCode);

	@Query(value = "SELECT * FROM tbl_city WHERE (LOWER(REPLACE(city_name, ' ', '')) = :cityName or LOWER(REPLACE(city_code, ' ', '')) = :cityName)", nativeQuery = true)
	List<City> findByCityName(@Param("cityName") String location);
	
	@Query(value = "SELECT * FROM tbl_city WHERE (LOWER(REPLACE(city_name, ' ', '')) = :cityName or LOWER(REPLACE(city_code, ' ', '')) = :cityName) AND status='ACTIVE'", nativeQuery = true)
	List<City> findByCityNameWithStatus(@Param("cityName") String location);
	
	@Query(value = "SELECT * FROM tbl_city WHERE (LOWER(REPLACE(city_name, ' ', '')) = :cityName or LOWER(REPLACE(city_code, ' ', '')) = :cityName)AND state_code=:stateCode AND status='ACTIVE' LIMIT 1", nativeQuery = true)
	Optional<City> findByCityNameWithStateCodeAndStatus(@Param("cityName") String cityName,@Param("stateCode") String stateCode);
	
	@Query(value = "SELECT * FROM tbl_city WHERE (LOWER(REPLACE(city_name, ' ', ''))) = :cityName AND state_code =:stateCode", nativeQuery = true)
	List<City> findByCityNameAndStateCode(@Param("cityName") String cityName,@Param("stateCode") String stateCode);
	
	@Query(value="SELECT * FROM tbl_city where status='ACTIVE' AND state_code=:stateCode",nativeQuery =true)
	List<City> findAllBystateCode(String stateCode);


      @Query(value = "SELECT city_name FROM tbl_city WHERE city_code = :cityCode and status='ACTIVE'", nativeQuery = true)
        Optional<String> findByCityCode(@Param("cityCode") String cityCode);

      
    @Query(value="SELECT COALESCE(MAX(CAST(SUBSTRING(city_code FROM 4) AS INTEGER)), 0) AS lastSequence FROM tbl_city",nativeQuery = true)
    String getLastSequence();




}
