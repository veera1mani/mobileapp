package com.healthtraze.etraze.api.report.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.report.model.ColumnConfigDetails;
import com.healthtraze.etraze.api.report.model.ReportDefinition;
import com.healthtraze.etraze.api.report.repository.ReportDefinitionRepository;
import com.healthtraze.etraze.api.report.service.ReportsService;


@CrossOrigin
@RestController
@RequestMapping("/rest/api/v1/report")
public class ReportController {

	
	
	private Logger logger = LogManager.getLogger(ReportController.class);
	
	
	
	private final ReportDefinitionRepository reportDefinitionRepository;
	private final ReportsService reportsService;
	
	public ReportController(ReportDefinitionRepository reportDefinitionRepository,ReportsService reportsService) {
		this.reportDefinitionRepository=reportDefinitionRepository;
		 this.reportsService=reportsService;
		
	}
	

	@RequestMapping("/salesreport/{type}")
	public Result<String> doLogout(@PathVariable String type){
		Result<String> result = new Result<>();
		result.setCode("0000");
		logger.info("");	
		return result;
	}
	
	@GetMapping(value = "/reports")
	public List<HashMap<String , String>> reports(){
	    List<ReportDefinition> reports = reportDefinitionRepository.findAll()
	                                .stream()
	                                .filter(a -> a.getStatus().equals("ACTIVE"))
	                                .sorted(Comparator.comparing(ReportDefinition::getOrderBy))
	                                .collect(Collectors.toList());

	   return  reports.stream()
	                                .map(r -> {
	                                    HashMap<String , String> map = new HashMap<>();
	                                    map.put("reportId", r.getReportId());
	                                    map.put("reportName", r.getReportName());
	                                    map.put("reportType", r.getReportType());
	                                    map.put("orderBy", String.valueOf(r.getOrderBy()));
	                                    return map;
	                                })
	                                .collect(Collectors.toList());  
	}
	
	
	
	@GetMapping(value = "/columns/{reportid}")
	public List<ColumnConfigDetails> findReportColumnDefinition(@PathVariable String reportid ){
		return reportsService.findReportById(reportid);
	}
	
	
	

	@GetMapping(value = "/{reportid}")
	public ResponseEntity<Resource> findReportDetail(@PathVariable String reportid,@RequestParam(required = false) Map<String,String> params,HttpServletRequest request ){
		try {
		
		return getResposeEntity(null,request);	
		}catch(Exception e) {
			logger.error("", e); 
			  return ResponseEntity.noContent().build();
		}
	}
	
	
	@PostMapping("/excel/{reportid}")
	@SuppressWarnings("unchecked")

	public List<ReportsService> findReportDetailExcel(@PathVariable String reportid,@RequestBody(required = false) Map<String,String> params,HttpServletRequest request ) {

		Map<String, String> map =  new LinkedHashMap<>(); 
		try {
		
			if(params!= null) {
				
				map = params;
			}
			}
			catch (Exception e) {
		logger.error(e);
			}

		return reportsService.findReportDetailExcel(reportid,map);
		

	}

	  
		
	
	 private ResponseEntity<Resource> getResposeEntity(Resource resource, HttpServletRequest request) {
		  String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	            logger.info("Could not determine file type.");
	        }


	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);

	}

	 
	
}
