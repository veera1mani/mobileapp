package com.healthtraze.etraze.api.masters.dto;

import java.util.List;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTransporterDTO {
	
	private List<TicketOrderInvoice> tickets;
	private String transporterId;
	private String vehicleNo;
	private String stockistName;
	private String invoiceNumber;
	private String location;
	private List<List<TicketReportsDTO>> list;
}
