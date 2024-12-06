package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Ticket;

public class TicketSummary {

	private int total;
	private int assigned;
	private int unAssigned;
	private int completed;
	private int inProcess;

	private List<Ticket> tickets;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		
		this.total = total;
	}

	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

	public int getUnAssigned() {
		return unAssigned;
	}

	public void setUnAssigned(int unAssigned) {
		this.unAssigned = unAssigned;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getInProcess() {
		return inProcess;
	}

	public void setInProcess(int inProcess) {
		this.inProcess = inProcess;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

}
