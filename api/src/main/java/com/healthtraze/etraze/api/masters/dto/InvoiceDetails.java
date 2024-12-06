package com.healthtraze.etraze.api.masters.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceDetails {
	
	private String manufacturer;
	private String invoice;
	private String cartons;
	private Date invoiceDate;
	private String invoicevalue;

}
