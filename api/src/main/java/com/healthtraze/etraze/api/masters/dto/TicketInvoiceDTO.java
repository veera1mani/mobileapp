package com.healthtraze.etraze.api.masters.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TicketInvoiceDTO {

	private	String ticketId;
	private	String invoiceNumber;
	private	String invoiceValue;
	private	String lineItems;
	private	String stockistName;
	private	String location;
	private	String status;
	private	String numOfCases;
	
	private	String subject;
	private	String createdOn;
	private	String emailedBy;
	private	String userName;
	
	private	String transporterName;
	private	String vehicaleNo;
	private	String lrNumber;
	private	String lrRecivedDate;
	private	String lrDocument;
	private	String address;
	private String deliveryDate;
	private String eml;
	private Object sentDate;
	private String id ;
	private String ccdRequired;
	private String podRequired;
	private Date dueDate;
	
	private String filePath;
	
}
