package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ManufacturerReportMapping;

@Repository
public interface ManufacturerReportMappingRepository extends BaseRepository<ManufacturerReportMapping, String> {

	 @Query(value = "select * from tbl_manufacturer_report_mapping where manufacturer_id=:manufacturerId",nativeQuery = true)
	List<ManufacturerReportMapping> findAllmammm(String manufacturerId);

}
