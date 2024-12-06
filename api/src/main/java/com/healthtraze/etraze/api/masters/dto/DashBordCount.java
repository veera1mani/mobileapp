package com.healthtraze.etraze.api.masters.dto;

import java.math.BigInteger;

public class DashBordCount {
	
	private BigInteger tenant;
	private BigInteger users;
	private BigInteger transporter;
	private BigInteger stockist;
	private BigInteger manufacturer;
	private BigInteger orderCount;
	private BigInteger returnCount;
	public BigInteger getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(BigInteger orderCount) {
		this.orderCount = orderCount;
	}
	public BigInteger getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(BigInteger returnCount) {
		this.returnCount = returnCount;
	}
	private BigInteger tenantUser;
	private BigInteger tenantManager;
	private BigInteger tenantStockist;
	private BigInteger tenantTransport;
	
	public BigInteger getTenantUser() {
		return tenantUser;
	}
	public void setTenantUser(BigInteger tenantUser) {
		this.tenantUser = tenantUser;
	}
	public BigInteger getTenantManager() {
		return tenantManager;
	}
	public void setTenantManager(BigInteger tenantManager) {
		this.tenantManager = tenantManager;
	}
	public BigInteger getTenantStockist() {
		return tenantStockist;
	}
	public void setTenantStockist(BigInteger tenantStockist) {
		this.tenantStockist = tenantStockist;
	}
	public BigInteger getTenantTransport() {
		return tenantTransport;
	}
	public void setTenantTransport(BigInteger tenantTransport) {
		this.tenantTransport = tenantTransport;
	}
	public BigInteger getTenant() {
		return tenant;
	}
	public void setTenant(BigInteger tenant) {
		this.tenant = tenant;
	}
	public BigInteger getUsers() {
		return users;
	}
	public void setUsers(BigInteger users) {
		this.users = users;
	}
	public BigInteger getTransporter() {
		return transporter;
	}
	public void setTransporter(BigInteger transporter) {
		this.transporter = transporter;
	}
	public BigInteger getStockist() {
		return stockist;
	}
	public void setStockist(BigInteger stockist) {
		this.stockist = stockist;
	}
	public BigInteger getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(BigInteger manufacturer) {
		this.manufacturer = manufacturer;
	}
	
		
	

}