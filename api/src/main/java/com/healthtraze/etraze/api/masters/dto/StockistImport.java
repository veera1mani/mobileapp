package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockistImport {
	
	private String message=StringIteration.EMPTY_STRING;
	
	private Stockist stockist;
	
	private boolean flag;
	
	private Row row;
	
	private StockistManufacture stockistManufacture;
	
	public void setMessage(String message) {
		this.message =this.message+ message;
	}

	public StockistImport(Stockist stockist, Row row) {
		super();
		this.stockist = stockist;
		this.row = row;
	}

	public StockistImport(String message, Stockist stockist, boolean flag, Row row) {
		super();
		this.message = message;
		this.stockist = stockist;
		this.flag = flag;
		this.row = row;
	}

	public StockistImport(Stockist stockist, boolean flag, Row row) {
		super();
		this.stockist = stockist;
		this.flag = flag;
		this.row = row;
	}
	
	public StockistImport(Stockist stockist, boolean flag, Row row,StockistManufacture stockistManufacture) {
		super();
		this.stockist = stockist;
		this.flag = flag;
		this.row = row;
		this.stockistManufacture = stockistManufacture;
	}
	
	
	

}
