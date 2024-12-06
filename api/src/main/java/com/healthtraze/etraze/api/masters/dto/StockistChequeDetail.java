package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Cheque;
import com.healthtraze.etraze.api.masters.model.Stockist;

public class StockistChequeDetail {

	private Stockist stockist;
	private List<Cheque> cheques;
	private int inHand;
	private String user;
	
	public Stockist getStockist() {
		return stockist;
	}

	public void setStockist(Stockist stockist) {
		this.stockist = stockist;
	}

	public List<Cheque> getCheques() {
		return cheques;
	}

	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}

	public int getInHand() {
		return inHand;
	}

	public void setInHand(int inHand) {
		this.inHand = inHand;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
