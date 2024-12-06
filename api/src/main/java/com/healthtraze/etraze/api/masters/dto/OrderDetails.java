package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketStatusHistory;

public class OrderDetails {

	private Ticket  ticket;
	
	private List<TicketStatusHistory> history;
	
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
	
	
	
	
}
