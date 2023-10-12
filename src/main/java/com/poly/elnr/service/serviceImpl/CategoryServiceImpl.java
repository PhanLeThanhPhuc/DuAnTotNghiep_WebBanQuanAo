package com.poly.elnr.service.serviceImpl;

import com.poly.elnr.entity.Category;
import com.poly.elnr.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findALlCategory() {
        return categoryRepository.findAll();
    }
}
