package com.healthtraze.etraze.api.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseRestController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.dashboard.service.DashboardService;
import com.healthtraze.etraze.api.masters.dto.DashBordCount;
import com.healthtraze.etraze.api.masters.dto.ManagerCountDTO;
import com.healthtraze.etraze.api.masters.dto.ManufacturerDto;
import com.healthtraze.etraze.api.masters.dto.TenantDashBoardDto;


@RestController
public class DashboardController implements BaseRestController{

	
	private final DashboardService dashboardService;
	
	@Autowired
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

		
		
		
	@GetMapping(value = "/total-count")
	public DashBordCount finddashboardCount(@RequestParam(required=false) String tenantId) {
	    return dashboardService.finddashboardCount(tenantId);
	}

	
	@GetMapping(value = "/chart-seriess")
	public Map<String,Object> finddashboardCharts() {
	    return dashboardService.finddashboardCharts();
	}
	
	@GetMapping(value="orders-count")
	public Result<HashMap<String,Object>> dashBoardOrders(@RequestParam String assignedTo){
		
		return dashboardService.dashBoardOrders(assignedTo);
	}
	
	
   @GetMapping(value = "/manager-count")
   public ManagerCountDTO findTicketManagerCount() {
   return  dashboardService.findTicketManagerCount();
}
   
   
   @GetMapping(value = "/tenant-count")
   public Map <String,Object > findTenantCount(@RequestParam(required=false) String manufacturerId,@RequestParam (required=false) Integer month) {
	   return dashboardService.findTenantCount(manufacturerId,month);
	 	   
}
   @GetMapping(value = "/order-dashboard")
   public TenantDashBoardDto  orderDashBoard(@RequestParam(required=false) String manufacturerId,@RequestParam (required=false) Integer month) {
	   return dashboardService.findOrderDashboard(manufacturerId,month);
	 	   
}
   @GetMapping(value = "/cheque-dashboard")
   public TenantDashBoardDto  chequeDashBoard(@RequestParam(required=false) String manufacturerId,@RequestParam (required=false) Integer month) {
	   return dashboardService.findchequeDashBoard(manufacturerId,month);
	 	   
}
   @GetMapping(value = "/return-dashboard")
   public TenantDashBoardDto  returnsdashBoard(@RequestParam(required=false) String manufacturerId,@RequestParam (required=false) Integer month) {
	 return dashboardService.findreturnsdashBoard(manufacturerId,month);
	 	   
}
   
   
   @GetMapping(value="/carton-count")
	public Map<String,Object> dashboardCarton(@RequestParam (required=false)  String transporter,@RequestParam (required=false)  String manufacturerId,@RequestParam (required=false)  Integer month){
		
		return dashboardService.dashboardCarton(transporter,manufacturerId,month);
	}
   @GetMapping(value="/monthly-missed")
  	public TenantDashBoardDto monthlyMissed(@RequestParam (required=false)  String transporter,@RequestParam (required=false)  String manufacturerId,@RequestParam (required=false)  Integer month){
  		
  		return dashboardService.monthlyMissed(manufacturerId,month);
  	}
     
   
   @GetMapping(value = "/tenantdetails-count/{tenantId}")
	public DashBordCount findtenantdetailscount(@PathVariable("tenantId") String tenantId) {
	    return dashboardService.findtenantdetailsCount(tenantId);
	}
   
   @GetMapping(value = "/manufacture-counts")
  	public ManufacturerDto manufacturerDashBoard() {
  	    return dashboardService.manufacturerDashBoard();
  	}
   
   
   @GetMapping(value = "/manufacture-tenant")
  public List<ManufacturerDto> manufaDashBoardDBytenant() {
 	    return dashboardService.manufaDashBoardDBytenant();
 	}
}
