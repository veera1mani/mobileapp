package com.healthtraze.etraze.api.masters.model;

import java.util.List;

public class DashboardChart {
	 

	private String[] categories;
	private List<ChartSeries> series;
	
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public List<ChartSeries> getSeries() {
		return series;
	}
	public void setSeries(List<ChartSeries> series) {
		this.series = series;
	}
	



}
