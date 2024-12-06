package com.healthtraze.etraze.api.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooleanResult {
	
	private String message;
	private boolean flag;
	public BooleanResult(boolean flag) {
		super();
		this.flag = flag;
	}
	
	

}
