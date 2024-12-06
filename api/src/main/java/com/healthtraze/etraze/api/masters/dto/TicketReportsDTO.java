package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketReportsDTO {

	private String ticketNumber;
	private String manufacturer;
	private String emailDate;
	private String days;
	private String assignedTo;
	private String assignedOn;
	private String status;
	private String category;
	private String tenantId;
	private String tenantName;
	private String pinCode;
	private String manufacturerLocation;
	private String stockist;
	private String location;

	private String emailSubject;
	private String invoiceValue;

	private String invoiceNumber;
	private String invoiceDate;
	private String locality;

	private String transporter;
	private String vehicaleNo;
	private String noOfCases;
	private String remarks;
	private String completionDate;
	private String daysTaken;
	private String custoRefNo;
	private String sapId;

	private String dispatchedDate;
	private String deliveredDate;

	private String tldDays;
	private String mergedInvoice;
	private String distributionModel;

	private String stockistId;
	private String priority;

	private String packId;

	private boolean isSelected;
	
	private List<InvoiceDetails> cartonsDetails;
	
	private boolean multiQr;
	
	private int totalCartons;

}
