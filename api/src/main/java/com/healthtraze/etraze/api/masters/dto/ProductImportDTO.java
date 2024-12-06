package com.healthtraze.etraze.api.masters.dto;

import org.apache.poi.ss.usermodel.Row;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.masters.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImportDTO {
	
	private Product product;
	
	private boolean isValid;
	
	private Row row;
	
	private String message=StringIteration.EMPTY_STRING;
	
	public void setMessage(String message) {
		this.message = this.message+ message;
	}

	public ProductImportDTO(Product product, boolean isValid, Row row) {
		super();
		this.product = product;
		this.isValid = isValid;
		this.row = row;
	}

	public ProductImportDTO(Product product, String message) {
		super();
		this.product = product;
		this.message = message;
	}
	
	
	
	

}
