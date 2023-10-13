package com.poly.elnr.service.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Category;
import com.poly.elnr.repository.CategoryRepository;
import com.poly.elnr.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository dao;


	public List<Category> findAll() {
		return dao.findAll();
	}

	public Category findById(Integer id) {
		return dao.findById(id).get();
	}

	public Category create(Category category) {
		return dao.save(category);
	}

	public Category update(Category category) {
		return dao.save(category);
	}

	public void delete(Integer id) {
		
	}
}
