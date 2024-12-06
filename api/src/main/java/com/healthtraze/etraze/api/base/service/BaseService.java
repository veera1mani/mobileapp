package com.healthtraze.etraze.api.base.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.model.Result;



@Repository
public interface BaseService<T, T1> {
	public List<T> findAll();
	public T findById(T1 id);
	public Result<T> create(T t);
	public Result<T> update(T t);
	public Result<T> delete(T1 id);
	
	
}
