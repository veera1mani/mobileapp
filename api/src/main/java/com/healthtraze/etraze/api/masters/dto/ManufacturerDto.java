package com.healthtraze.etraze.api.masters.dto;

public class ManufacturerDto {
	
	
	private Integer returns;
	private Integer orders;
	private Integer tickets;
	
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	private String tenantName;
	
	public Integer getReturns() {
		return returns;
	}
	public void setReturns(Integer returns) {
		this.returns = returns;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	public Integer getTickets() {
		return tickets;
	}
	public void setTickets(Integer tickets) {
		this.tickets = tickets;
	}
	
}
