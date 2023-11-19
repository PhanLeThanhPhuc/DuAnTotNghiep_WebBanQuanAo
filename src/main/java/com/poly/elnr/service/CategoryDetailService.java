package com.poly.elnr.service;

import java.util.List;
import java.util.Map;

import com.poly.elnr.entity.CategoryDetail;


public interface CategoryDetailService {

	public List<CategoryDetail> findAll();

	public CategoryDetail create(CategoryDetail create);

	public void delete(CategoryDetail id);


	public CategoryDetail update(CategoryDetail create);

	public List<CategoryDetail> findByCategoryID(Integer id);
	
}
