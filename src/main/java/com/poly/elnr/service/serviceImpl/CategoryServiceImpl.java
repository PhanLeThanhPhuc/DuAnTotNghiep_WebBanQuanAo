package com.poly.elnr.service.serviceImpl;

import java.util.List;

import com.poly.elnr.entity.Category;
import com.poly.elnr.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Category;
import com.poly.elnr.repository.CategoryRepository;
import com.poly.elnr.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findById(Integer id) {
		return categoryRepository.findById(id).get();
	}

	public Category create(Category category) {
		return categoryRepository.save(category);
	}

	public Category update(Category category) {
		return categoryRepository.save(category);
	}

	public void delete(Integer id) {

	}

	@Override
	public List<Category> findALlCategory() {
		return categoryRepository.findAll();
	}
}
