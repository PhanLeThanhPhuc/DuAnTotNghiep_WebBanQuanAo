package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Description;


public interface DescriptionService {
	public List<Description> findAll() ;

	public Description findById(Integer id);

	public void create(Description description);

	public Description update(Description description);

	public void delete(Integer id);
}
