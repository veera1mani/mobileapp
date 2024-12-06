package com.healthtraze.etraze.api.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.masters.service.ClaimService;
import com.healthtraze.etraze.api.masters.service.ReturnService;
import com.healthtraze.etraze.api.masters.service.StockistService;
import com.healthtraze.etraze.api.masters.service.TicketReportService;
import com.healthtraze.etraze.api.report.model.ColumnConfigDetails;
import com.healthtraze.etraze.api.report.repository.ReportDefinitionRepository;

@Service

public class ReportsService {
	

    private   ReportDefinitionRepository reportDefinitionRepository;
	
	private StockistService stockistServices;
	private  ClaimService claimService;
	private ReturnService returnService;
   private TicketReportService ticketReportService;
   
      @Autowired
      public ReportsService(ReportDefinitionRepository reportDefinitionRepository, StockistService stockistServices,ClaimService claimService,ReturnService  returnService,TicketReportService ticketReportService) {
	    this.reportDefinitionRepository=reportDefinitionRepository;
	    this.claimService=claimService;
	    this.returnService=returnService;
	    this.ticketReportService=ticketReportService;
	    this.stockistServices=stockistServices;
	   
   }
   
   
	private Logger logger = LogManager.getLogger(ReportsService.class);

	public List<ColumnConfigDetails> findReportById(String reportid) {
		try {
			return reportDefinitionRepository.findReportColumnConfigsById(reportid);
		} catch (Exception e) {
			logger.error("", e);
		}
		return reportDefinitionRepository.findReportColumnConfigsById(reportid);

	}

	public List findReportDetailExcel(String reportid, Map<String, String> params) {
		
		List<ColumnConfigDetails> columnConfigDetails = findReportById(reportid);

		if (columnConfigDetails != null && !columnConfigDetails.isEmpty()) {
			ColumnConfigDetails columConfigDetails = columnConfigDetails.get(0);
			if (columConfigDetails.getReportId().equalsIgnoreCase("RP1001")) {
				return ticketReportService.ticketDetailsReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP1002")) {
				return ticketReportService.orderDetailsReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP1003")) {
				return ticketReportService.shipmentDetailsReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP1004")) {
				return stockistServices.chequeDetailsReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP1005")) {
				return stockistServices.chequeInHandReport(params);
			}else if (columConfigDetails.getReportId().equalsIgnoreCase("RP2001")) {
				return ticketReportService.ticketDetailsReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP2002")) {
				return ticketReportService.orderDeadlineReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP2003")) {
				return claimService.claimsStatusReport(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP2004")) {
				return stockistServices.chequeStatusReport(params);
			} 			
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP3002")) {
				return claimService.claimOverviewReport(params);
			} 			
			
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4001")) {
				 return ticketReportService.ticketNotClosed(params);			
			} 			
			 else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4004")) {
				return ticketReportService.dispacthedNotDelivered(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4005")) {
				return ticketReportService.blockedOrder(params);
				}
				else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4017")) {
					return ticketReportService.delivered(params);

			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4006")) {
				return returnService.claimsNotClosed(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4007")) {
				return returnService.receivedNotChecked(params);
			} else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4008")) {
           	return returnService.secondCheckNotCompleted(params);	
			}
			
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4011")) {
				return stockistServices.chequeInHandReport(params);	
			}
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4012")) {
				return stockistServices.chequeInHandReport(params);	
			}
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4013")) {
				return stockistServices.chequeDiffrentBank(params);	
			}
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4014")) {
				return stockistServices.chequeInHandReport(params);	
			}
			
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4015")) {
				return returnService.returndetailsReport(params);	
			}
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4016")) {
				return claimService.claimNotReecived(params);	
		}
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4018")) {
				return stockistServices.paymentReceived(params);	
		}
			
			else if (columConfigDetails.getReportId().equalsIgnoreCase("RP4020")) {
					return ticketReportService.lrNotreceived(params);	
					}
			
			
			


			
		}
			

		
			
		return new ArrayList<>();

	}
	
	
	

}
	
