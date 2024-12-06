package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketOrder;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;

public class TicketOrderDTO {	
	private TicketOrder ticketOrder;
	private Ticket ticket;
	private List<TicketOrderInvoice> invoices;
	public String getTeamMember() {
		return teamMember;
	}
	public void setTeamMember(String teamMember) {
		this.teamMember = teamMember;
	}
	private  String teamMember;
	private int ordresCreated;
	private String totalLineItems;
	private int ticketsCreated;
	private int ticketsClosed;
	
	
	public int getOrdresCreated() {
		return ordresCreated;
	}
	public void setOrdresCreated(int ordresCreated) {
		this.ordresCreated = ordresCreated;
	}

	
	public String getTotalLineItems() {
		return totalLineItems;
	}
	public void setTotalLineItems(String string) {
		this.totalLineItems = string;
	}
	public int getTicketsCreated() {
		return ticketsCreated;
	}
	public void setTicketsCreated(int ticketsCreated) {
		this.ticketsCreated = ticketsCreated;
	}
	public int getTicketsClosed() {
		return ticketsClosed;
	}
	public void setTicketsClosed(int ticketsClosed) {
		this.ticketsClosed = ticketsClosed;
	}
	public TicketOrder getTicketOrder() {
		return ticketOrder;
	}
	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}
	public List<TicketOrderInvoice> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<TicketOrderInvoice> invoices) {
		this.invoices = invoices;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	

}
