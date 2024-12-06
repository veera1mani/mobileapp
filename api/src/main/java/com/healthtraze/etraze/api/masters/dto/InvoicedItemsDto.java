package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import lombok.Data;

@Data
public class InvoicedItemsDto {
	
	private String invoiceNo;
	private String ticketId;
	private String invoiceValue;
	private String lineItems;
	private String status;
	private List<ItemsDto> items;
}
