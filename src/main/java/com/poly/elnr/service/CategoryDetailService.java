package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.dto.CategoryDTO;

public interface CategoryDetailService {

	List<CategoryDTO> findCategoryAndCategoryDetail();
	
}
