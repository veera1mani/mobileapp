package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Cheque;
import com.healthtraze.etraze.api.masters.model.ChequeStatusHistory;
import com.healthtraze.etraze.api.masters.model.Stockist;

public class ChequeDetailDTO {
	
	private Cheque cheque;
	
	private Stockist stockist;
	
	private List<ChequeStatusHistory> history;

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	public Stockist getStockist() {
		return stockist;
	}

	public void setStockist(Stockist stockist) {
		this.stockist = stockist;
	}

	public List<ChequeStatusHistory> getHistory() {
		return history;
	}

	public void setHistory(List<ChequeStatusHistory> history) {
		this.history = history;
	}
	
	

}
