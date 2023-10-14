package com.poly.elnr.service;


import com.poly.elnr.entity.Category;

import java.util.List;

public interface CategoryService {
	public List<Category> findAll() ;

	public Category findById(Integer id) ;

	public Category create(Category category) ;

	public Category update(Category category) ;

	public void delete(Integer id) ;

    List<Category> findALlCategory();

}
