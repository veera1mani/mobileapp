package com.healthtraze.etraze.api.masters.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO {
	
	private int size=10;
	private String search;
	private String sortBy;
	private String sortDir;
	private int page;
	private String status;
	
	public PaginationDTO(String search, String sortBy, String sortDir) {
		super();
		this.search = search;
		this.sortBy = sortBy;
		this.sortDir = sortDir;
	}
	public PaginationDTO(String sortBy, String sortDir) {
		super();
		this.sortBy = sortBy;
		this.sortDir = sortDir;
	}
	public PaginationDTO(String search, String sortBy, String sortDir, int page) {
		super();
		this.search = search;
		this.sortBy = sortBy;
		this.sortDir = sortDir;
		this.page = page;
	}
	public PaginationDTO(String search, String sortBy, String sortDir, int page,String status) {
		super();
		this.search = search;
		this.sortBy = sortBy;
		this.sortDir = sortDir;
		this.page = page;
		this.status = status;
	}
	
	
	
}