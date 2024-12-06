package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.Bank;


@Repository
public interface BankRepository extends BaseRepository<Bank, String>{

	
	public Optional<Bank> findByName(String bankName);
	
	
	@Query(value="SELECT * FROM tbl_bank bank WHERE  bank.code  ILIKE %:value% OR bank.name ILIKE %:value% OR Status ILIKE %:value% " ,nativeQuery = true)
	public List<Bank> findAllBank(String value, Pageable paging);
	
	@Query(value="SELECT * FROM tbl_bank bank WHERE  bank.code  ILIKE %:value% OR bank.name ILIKE %:value% OR Status ILIKE %:value% " ,nativeQuery = true)
	public List<Bank> findAllBank(String value);

	
	@Query(value="select * FROM tbl_bank",nativeQuery = true)
	public List<Bank> findAllCount();
	
	@Query(value="select * FROM tbl_bank where status='ACTIVE'",nativeQuery = true)
	public List<Bank> findAllBanks();
	
	@Query(value="select * FROM tbl_bank where LOWER(code)=LOWER(:bankName)",nativeQuery = true)
	public List<Bank> findBanksByName(@Param("bankName")String name);

	@Query(value="select * from tbl_bank where LOWER(code)=LOWER(:bankName) or LOWER (name)=LOWER(:bankName)",nativeQuery = true)
	public List<Bank> findBankName(@Param("bankName")String name);

	
	@Query(value = "SELECT * FROM tbl_bank WHERE ((LOWER(REPLACE(code, ' ', '')) = :bankId or LOWER(REPLACE(name, ' ', '')) = :bankId)) AND status='ACTIVE'  ", nativeQuery = true)
	Optional<Bank> findByBankId(@Param("bankId") String bankId);

	

	

	

}
