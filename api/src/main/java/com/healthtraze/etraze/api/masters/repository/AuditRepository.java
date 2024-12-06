package com.healthtraze.etraze.api.masters.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.Audit;
@Repository
public interface  AuditRepository extends JpaRepository<Audit, String>{

  
	

	

}