package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
 public class TransporterChartDTO {
	
	private String name;
	private String type="column";
	private List<Long> data;
	

}
