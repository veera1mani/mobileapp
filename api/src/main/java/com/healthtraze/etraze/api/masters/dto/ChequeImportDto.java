package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.masters.model.Cheque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChequeImportDto {

	private String message=StringIteration.EMPTY_STRING;
	
	private boolean flag;
	
	private Row row;
	
	private Cheque cheque;
	
	private boolean threshold ;
	
	private String receivedVia;
	
	private String invalidChequeNo = StringIteration.EMPTY_STRING;
	
	public void setMessage(String message) {
		this.message =this.message+ message;
	}

	public void setInvalidChequeNo(String cNo) {
		this.invalidChequeNo = this.invalidChequeNo+ cNo;
	}
	
	public ChequeImportDto(boolean flag, Row row, Cheque cheque, boolean threshold) {
		super();
		this.flag = flag;
		this.row = row;
		this.cheque = cheque;
		this.threshold = threshold;
	}
	
	
}
