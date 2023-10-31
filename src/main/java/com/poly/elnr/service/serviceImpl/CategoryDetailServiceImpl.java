package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.CategoryDetail;
import com.poly.elnr.repository.CategoryDetailRepository;
import com.poly.elnr.repository.ColorRepository;
import com.poly.elnr.service.CategoryDetailService;

@Service
public class CategoryDetailServiceImpl implements CategoryDetailService {
	@Autowired
	CategoryDetailRepository categorydetailRepository ;
	@Override
	public List<CategoryDetail> findAll() {
		// TODO Auto-generated method stub
		return categorydetailRepository.findAll();
	}

	@Override
	public CategoryDetail create(CategoryDetail categorydetail) {
		// TODO Auto-generated method stub
		return categorydetailRepository.save(categorydetail);
	}

	@Override
	public void delete(CategoryDetail id) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public CategoryDetail update(CategoryDetail create) {
		return categorydetailRepository.save(create);
	}

//	@Override
//	public List<CategoryDetail> findById(int id) {
//		return categorydetailRepository.findById(id).get();
//	}

}
