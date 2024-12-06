package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderImport {
	
	private String message=StringIteration.EMPTY_STRING;
	
	private boolean flag;
	
	private Row row;
	
	private OrderDTO orderdto;
	
	public void setMessage(String message) {
		this.message =this.message+ message;
	}

	public OrderImport(boolean flag, Row row, OrderDTO orderdto) {
		super();
		this.flag = flag;
		this.row = row;
		this.orderdto = orderdto;
	}

}
