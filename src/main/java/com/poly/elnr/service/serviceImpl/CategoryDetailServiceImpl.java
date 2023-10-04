package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.dto.CategoryDTO;
import com.poly.elnr.repository.CategoryDetailRepository;
import com.poly.elnr.service.CategoryDetailService;

@Service
public class CategoryDetailServiceImpl implements CategoryDetailService{

	@Autowired
	CategoryDetailRepository categoryDetailRepository;
	
	@Override
	public List<CategoryDTO> findCategoryAndCategoryDetail() {
		// TODO Auto-generated method stub
		return categoryDetailRepository.findCategoryAndCategoryDetail();
	}

}
