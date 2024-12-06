package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.masters.model.TransporterDelivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterImport {
	
	private String message=StringIteration.EMPTY_STRING;
	
	private boolean flag;
	
	private Row row;
	
	private TransporterDelivery transporterDelevory ;
	
	public void setMessage(String message) {
		this.message =this.message+ message+",";
	}

	public TransporterImport(boolean flag, Row row) {
		super();
		this.flag = flag;
		this.row = row;
	}

	public TransporterImport(boolean flag, Row row, TransporterDelivery transporterDelevory) {
		super();
		this.flag = flag;
		this.row = row;
		this.transporterDelevory = transporterDelevory;
	}

}
