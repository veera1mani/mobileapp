package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.dto.TransporterAmountChartDTO;
import com.healthtraze.etraze.api.masters.dto.TransporterChartDTO;
import com.healthtraze.etraze.api.masters.model.TransporterDelivery;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TransporterDeliveryRepository extends BaseRepository<TransporterDelivery,String> {

	@Query(value = "SELECT * FROM tbl_transporter_delivery WHERE TO_CHAR(delivery_date, 'MM') = :givenMonth AND TO_CHAR(delivery_date, 'YYYY') = :givenYear AND tenant_id = :tenantId AND manufacturer_id = :manfId AND transporter_id = :tid",nativeQuery= true)
	public Optional<TransporterDelivery> findByMonthAndYear(String givenMonth , String givenYear , String tenantId , String manfId , String tid  );
	
//	@Query(value = "SELECT  t.transport_name AS label,  EXTRACT(YEAR FROM td.delivery_date) AS year,  EXTRACT(MONTH FROM td.delivery_date) AS month, SUM(td.transporter_expenses) AS y FROM  tbl_transporter_delivery td LEFT JOIN tbl_transport t ON t.transport_id = td.transporter_id WHERE td.manufacturer_id=:manufacturerId AND td.tenant_id=:tenantId GROUP BY t.transport_name, EXTRACT(YEAR FROM td.delivery_date),  EXTRACT(MONTH FROM td.delivery_date) ORDER BY transport_name ASC",nativeQuery= true)
//	public List<dou> findByManufacturer(String manufacturerId, String tenantId);
	
	@Query(value = "select COALESCE(sum(transporter_expenses),0) from tbl_transporter_delivery t where t.manufacturer_id ILIKE %:manfId% AND t.tenant_id=:tenantId and t.transporter_id =:tid and EXTRACT(YEAR FROM t.delivery_date) =CAST(:year AS INTEGER) and EXTRACT(Month from t.delivery_date) =  CAST(:month  AS INTEGER) ",nativeQuery= true)
	public Long findTransporterExpenses(@Param("manfId")String manfId  , String tenantId , String tid , @Param("year") String year , @Param("month") String month);
	
	

	

}
