package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketStatusHistory;

public class TicketDetails {

	private Ticket  ticket;
	
	private List<TicketStatusHistory> history;

	private TicketOrderDTO orderDTO;
	
	private String stockistStatus;
	
	private boolean wms;

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public List<TicketStatusHistory> getHistory() {
		return history;
	}

	public void setHistory(List<TicketStatusHistory> history) {
		this.history = history;
	}

	public TicketOrderDTO getOrderDTO() {
		return orderDTO;
	}

	public void setOrderDTO(TicketOrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}

	public String getStockistStatus() {
		return stockistStatus;
	}

	public void setStockistStatus(String stockistStatus) {
		this.stockistStatus = stockistStatus;
	}

	public boolean isWms() {
		return wms;
	}

	public void setWms(boolean wms) {
		this.wms = wms;
	}
	
	
}
