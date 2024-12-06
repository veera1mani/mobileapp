package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.masters.model.ReturnNote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CnImport {
	
	private String message=StringIteration.EMPTY_STRING;
	
	private boolean flag;
	
	private Row row;
	
	private ReturnNote returnNote;
	
	private String manufacturer;
	
	private String grrnNumber;
	
	public void setMessage(String message) {
		this.message =this.message+ message+",";
	}

	public CnImport(boolean flag, Row row, ReturnNote returnNote) {
		super();
		this.flag = flag;
		this.row = row;
		this.returnNote = returnNote;
	}

	public CnImport(ReturnNote returnNote) {
		super();
		this.returnNote = returnNote;
	}
	
	

}
