package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Category;


public interface CategoryService {

	public List<Category> findAll() ;

	public Category findById(Integer id) ;

	public Category create(Category category) ;

	public Category update(Category category) ;

	public void delete(Integer id) ;
}
