package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ListValue;

@Repository
public interface ListValueRepository extends  BaseRepository<ListValue, String> {
		

	@Query(value="SELECT * FROM tbl_list_value lv WHERE " +
	        "lv.key ILIKE %:value% OR " +
	        "lv.description ILIKE %:value%", nativeQuery=true)
	List<ListValue> findAllValue(@Param("value") String value, Pageable paging);
	
	
	@Query(value="SELECT * FROM tbl_list_value lv WHERE " +
	        "lv.key ILIKE %:value% OR " +
	        "lv.description ILIKE %:value%", nativeQuery=true)
	List<ListValue> findAllValue(@Param("value") String value);
	
	
	@Query(value="SELECT * FROM tbl_list_value",nativeQuery = true)
	List<ListValue> findAllCount();

	@Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='700' and  LOWER (b.CODE) =:locality  ",nativeQuery = true)
	List<ListValue> findByLocality(String locality);
	
	@Query(value="select b.code   from list_value_values b where b.list_value_key=:key AND ((LOWER(REPLACE(b.code, ' ', '')) = :value or LOWER(REPLACE(b.name, ' ', '')) = :value)) LIMIT 1",nativeQuery = true)
	Optional<String> findByNameAndCode(@Param("key") String key,@Param("value") String value);
	
	@Query(value="select b.name from list_value_values b where b.list_value_key=:key AND b.code = :value LIMIT 1",nativeQuery = true)
	Optional<String> findByCode(@Param("key") String key,@Param("value") String value);
	
	@Query(value="select b.name,b.value from list_value_values b where b.list_value_key=:key AND b.code = :value LIMIT 1",nativeQuery = true)
	List<Object[]> findNameAndValueByCodeAndKey(@Param("key") String key,@Param("value") String value);
	
	@Query(value="select b.value  from list_value_values b where b.list_value_key=:key AND (b.code = :value or b.name = :value) LIMIT 1",nativeQuery = true)
	String findByNameAndCodeValue(@Param("key") String key,@Param("value") String value);
	
	@Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='700' and ((LOWER(REPLACE(b.code, ' ', '')) = :locality or LOWER(REPLACE(b.name, ' ', '')) = :locality))",nativeQuery = true)
	Optional<ListValue> findByLocalityName(String locality);
	
	@Query(value="select b.code from list_value_values b where b.list_value_key='200' AND (LOWER(REPLACE(b.code, ' ', '')) = :value or LOWER(REPLACE(b.name, ' ', '')) = :value)",nativeQuery = true)
	List<String> findByNameAndCodeChequeCatagery(@Param("value") String value);
	
	@Query(value="select b.code from list_value_values b where b.list_value_key='700' AND (LOWER(REPLACE(b.code, ' ', '')) = :value or LOWER(REPLACE(b.name, ' ', '')) = :value)",nativeQuery = true)
	List<String> findByNameAndCodeLocality(@Param("value") String value);
	
	@Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='700' and LOWER(REPLACE(b.CODE,' ',''))=:locality  ",nativeQuery = true)
	List<ListValue> findByLocalityEx(String locality);

    @Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='200' and  LOWER (b.CODE) =:cheque  ",nativeQuery = true)
    List<ListValue> findByChequeCategory(String cheque);
    
    @Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='200' and ((LOWER(REPLACE(b.code, ' ', '')) = :cheque or LOWER(REPLACE(b.name, ' ', '')) = :cheque)) ",nativeQuery = true)
    List<ListValue> findByChequeCategoryName(String cheque);
    
    @Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='200' and ((LOWER(REPLACE(b.code, ' ', '')) = :cheque or LOWER(REPLACE(b.name, ' ', '')) = :cheque)) ",nativeQuery = true)
    List<ListValue> findChequeCategoryByNameAndCode(String cheque);

	
	@Query(value = "SELECT b.code FROM list_value_values b WHERE b.code = :code", nativeQuery = true)
	String findByCode(@Param("code") String code);
	
	@Query(value = "SELECT b.value FROM list_value_values b WHERE b.code = :code", nativeQuery = true)
	String findByCodeAndValu(@Param("code") String code);
	
	@Query(value = "SELECT b.name FROM list_value_values b WHERE b.code = :code LIMIT 1", nativeQuery = true)
	Optional<String> findByCodee(@Param("code") String code);
	
	@Query(value = "SELECT b.code FROM list_value_values b WHERE b.code = :code", nativeQuery = true)
	Optional<String> findByiCode(@Param("code") String code);

    @Query(value="select * from tbl_list_value a inner join  list_value_values b on  b.list_value_key=a.key  where a.key='800' and  LOWER (b.CODE) =:cheque ",nativeQuery = true)
    List<ListValue> findByChequeReceivedVia(String cheque);

    @Query(value="select * from tbl_list_value a inner join  list_value_values b on b.list_value_key=a.key where a.key='300' and  LOWER (b.CODE) =:cheque ",nativeQuery = true)
    List<ListValue> findByReceivedVia(String cheque);
    
    @Query(value="select * from tbl_list_value a inner join  list_value_values b on b.list_value_key=a.key where a.key='300' and ((LOWER(REPLACE(code, ' ', '')) = :cheque or LOWER(REPLACE(name, ' ', '')) = :cheque)) ",nativeQuery = true)
    Optional<ListValue> findReceivedVia(String cheque);
    
    @Query(value = "SELECT b.name FROM list_value_values b WHERE b.code =:code LIMIT 1", nativeQuery = true)
	String findByCodeAndName(@Param("code") String code);

	

}