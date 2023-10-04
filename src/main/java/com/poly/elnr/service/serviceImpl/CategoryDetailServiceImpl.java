package com.poly.elnr.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.dto.CategoryDTO;
import com.poly.elnr.repository.CategoryDetailRepository;
import com.poly.elnr.service.CategoryDetailService;

@Service
public class CategoryDetailServiceImpl implements CategoryDetailService {

	@Autowired
	CategoryDetailRepository categoryDetailRepository;

	@Override
	public Map<String, List<String>> findCategoryAndCategoryDetail() {

		Map<String, List<String>> categoryMap = new HashMap<>();

		for (CategoryDTO cdto : categoryDetailRepository.findCategoryAndCategoryDetail()) {
			
			//check coi idCategory có trong map k??
			if (categoryMap.containsKey(cdto.getCatgoryName())) {
				categoryMap.get(cdto.getCatgoryName()).add(cdto.getCatgoryDetailName());
			} else {
				List<String> categoryDetailName = new ArrayList<>();
				categoryDetailName.add(cdto.getCatgoryDetailName());
				categoryMap.put(cdto.getCatgoryName(), categoryDetailName);
			}
			
		}

		return categoryMap;
	}

}
