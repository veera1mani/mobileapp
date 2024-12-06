package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Ticket;

public class OrderSummary {

	private int total;
	private int assigned;
	private int checked;
	private int packed;
	private int picked;
	private int podReceived;
	private int delivered;
	private int invoiceCreated;
	private int transporterAssigned;
	
	
	
	public int getDelivered() {
		return delivered;
	}

	public void setDelivered(int delivered) {
		this.delivered = delivered;
	}

	public int getCancel() {
		return cancel;
	}

	public void setCancel(int cancel) {
		this.cancel = cancel;
	}

	private int blocked;
	private int deliverd;
	private int dispatched;
	private int cancel;
	private List<Ticket> orders;

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

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public int getPacked() {
		return packed;
	}

	public void setPacked(int packed) {
		this.packed = packed;
	}

	public int getPicked() {
		return picked;
	}

	public void setPicked(int i) {
		this.picked = i;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public int getDeliverd() {
		return deliverd;
	}

	public void setDeliverd(int deliverd) {
		this.deliverd = deliverd;
	}

	public int getDispatched() {
		return dispatched;
	}

	public void setDispatched(int dispatched) {
		this.dispatched = dispatched;
	}

	public List<Ticket> getOrders() {
		return orders;
	}

	public void setOrders(List<Ticket> orders) {
		this.orders = orders;
	}

	public int getPodReceived() {
		return podReceived;
	}

	public void setPodReceived(int podReceived) {
		this.podReceived = podReceived;
	}

	public int getInvoiceCreated() {
		return invoiceCreated;
	}

	public void setInvoiceCreated(int invoiceCreated) {
		this.invoiceCreated = invoiceCreated;
	}

	public int getTransporterAssigned() {
		return transporterAssigned;
	}

	public void setTransporterAssigned(int transporterAssigned) {
		this.transporterAssigned = transporterAssigned;
	}
	
	
	
	
}
