package com.healthtraze.etraze.api.masters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.masters.model.OrderTransporterMapping;

@Repository
public interface OrderTransporterMappingRepository extends JpaRepository<OrderTransporterMapping, String>{

}
