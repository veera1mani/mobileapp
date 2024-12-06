package com.healthtraze.etraze.api.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.report.model.ColumnConfigDetails;
import com.healthtraze.etraze.api.report.model.ReportDefinition;

@Repository
public interface ReportDefinitionRepository extends BaseRepository<ReportDefinition, String> {

	@Query("select column.columnName AS columnName,column.columnKey AS columnKey,column.columnType AS columnType,"
			+ " def.reportName AS reportName,def.reportId AS reportId,column.width as width, "
			+ " column.isSumRequired AS isSumRequired,column.isCountRequired AS isCountRequired,column.isAvgRequired AS isAvgRequired,def.columnWiseSum AS columnWiseSum,"
			+ " def.columnWiseAvg AS columnWiseAvg,def.reportDesc as reportDesc,column.groupBy AS groupBy,column.groupBasedSum AS groupBasedSum "
			+ " from  ReportDefinition as def inner join ReportColumnConfig as column on column.reportId = def.reportId where def.reportId=:id  "
			+ " order by column.orderBy ")
	List<ColumnConfigDetails> findReportColumnConfigsById(@Param("id") String reportid);

}
