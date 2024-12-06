package com.healthtraze.etraze.api.base.controller;

import java.util.List;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.Result;

public interface BaseCrudController<T, T1> extends BaseRestController {

	public static final String SUCCESS = Constants.SUCCESS;
	public static final String ROLE_ADMIN = Constants.ROLEADMIN;
	public static final String ROLE_USER = Constants.ROLEUSER;

	public List<T> findAll();

	public T findById(T1 id);

	public Result<T> create(T t);

	public Result<T> update(T t);

	public Result<T> delete(T1 id);

}
