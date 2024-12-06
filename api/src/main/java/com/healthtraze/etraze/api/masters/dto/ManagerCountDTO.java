package com.healthtraze.etraze.api.masters.dto;

import java.math.BigInteger;

public class ManagerCountDTO {
	
	public int getOtherDeadline() {
		return otherDeadline;
	}
	public void setOtherDeadline(int otherDeadline) {
		this.otherDeadline = otherDeadline;
	}
	public int getOther() {
		return other;
	}
	public void setOther(int other) {
		this.other = other;
	}
	private BigInteger received;
	private BigInteger closed;
	private BigInteger totaltickets;
	private BigInteger overdueticketdeadlines;
	private BigInteger assigned;
	private BigInteger totalorder;
	private BigInteger overdueorderdeadlines;
	private BigInteger receivedreturns;
	private int cncreated;
	private int totalreturns;
	private int claimsdeadlinemissed;
	private BigInteger dispatched;
	private BigInteger delivered;
	private BigInteger overduedelivery;
	private int recievdTicket;
	public int getCloseTicket() {
		return closeTicket;
	}
	public void setCloseTicket(int closeTicket) {
		this.closeTicket = closeTicket;
	}
	public int getRecievdTicket() {
		return recievdTicket;
	}
	public void setRecievdTicket(int recievdTicket) {
		this.recievdTicket = recievdTicket;
	}
	private int closeTicket;
	private int otherDeadline;
	private int deadlineOrder;
	public int getDeadlineOrder() {
		return deadlineOrder;
	}
	public void setDeadlineOrder(int deadlineOrder) {
		this.deadlineOrder = deadlineOrder;
	}
	private int other;
	
		public BigInteger getDispatched() {
		return dispatched;
	}
	public void setDispatched(BigInteger dispatched) {
		this.dispatched = dispatched;
	}
	public BigInteger getOverduedelivery() {
		return overduedelivery;
	}
	public void setOverduedelivery(BigInteger overduedelivery) {
		this.overduedelivery = overduedelivery;
	}
	public int getTotalreturns() {
		return totalreturns;
	}
	public void setTotalreturns(int totalreturns) {
		this.totalreturns = totalreturns;
	}
	public BigInteger getReceivedreturns() {
		return receivedreturns;
	}
	public void setReceivedreturns(BigInteger receivedreturns) {
		this.receivedreturns = receivedreturns;
	}
	public int getCncreated() {
		return cncreated;
	}
	public int getClaimsdeadlinemissed() {
		return claimsdeadlinemissed;
	}
	public void setClaimsdeadlinemissed(int claimsdeadlinemissed) {
		this.claimsdeadlinemissed = claimsdeadlinemissed;
	}
	public void setCncreated(int cncreated) {
		this.cncreated = cncreated;
	}
	public BigInteger getAssigned() {
		return assigned;
	}
	public void setAssigned(BigInteger assigned) {
		this.assigned = assigned;
	}
	public BigInteger getDelivered() {
		return delivered;
	}
	public void setDelivered(BigInteger delivered) {
		this.delivered = delivered;
	}
	
	public BigInteger getTotaltickets() {
		return totaltickets;
	}
	public void setTotaltickets(BigInteger totaltickets) {
		this.totaltickets = totaltickets;
	}
	public BigInteger getReceived() {
		return received;
	}
	public void setReceived(BigInteger received) {
		this.received = received;
	}
	public BigInteger getClosed() {
		return closed;
	}
	public void setClosed(BigInteger closed) {
		this.closed = closed;
	}
	
	public BigInteger getTotalorder() {
		return totalorder;
	}
	public void setTotalorder(BigInteger totalorder) {
		this.totalorder = totalorder;
	}
	public BigInteger getOverdueticketdeadlines() {
		return overdueticketdeadlines;
	}
	public void setOverdueticketdeadlines(BigInteger overdueticketdeadlines) {
		this.overdueticketdeadlines = overdueticketdeadlines;
	}
	public BigInteger getOverdueorderdeadlines() {
		return overdueorderdeadlines;
	}
	public void setOverdueorderdeadlines(BigInteger overdueorderdeadlines) {
		this.overdueorderdeadlines = overdueorderdeadlines;
	}
	


}
