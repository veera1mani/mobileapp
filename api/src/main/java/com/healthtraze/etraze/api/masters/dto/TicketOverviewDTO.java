package com.healthtraze.etraze.api.masters.dto;


public class TicketOverviewDTO {
	
	private String category;
	private String received;
	private String closed;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getReceived() {
		return received;
	}
	public void setReceived(String received) {
		this.received = received;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	
	
}
