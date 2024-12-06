package com.healthtraze.etraze.api.masters.dto;

import java.math.BigInteger;

public interface ChequeListDTO {

	public String getStockistId();
	public String getStockistName();
	public String getSapId();
	public String getManufacturerName();
	public String getChequeCategory();
	public String getCity();
	public BigInteger getTotal();
	public BigInteger getInhand();
	public BigInteger getHold();
	public BigInteger getSecurity();
	public String getStatus();

}
