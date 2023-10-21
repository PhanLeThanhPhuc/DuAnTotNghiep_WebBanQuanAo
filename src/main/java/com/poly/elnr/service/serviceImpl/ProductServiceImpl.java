package com.poly.elnr.service.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ColorRepository;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.repository.SizeRepository;
import com.poly.elnr.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	SizeRepository sizeRepository;

	@Autowired
	ColorRepository colorRepository;

	@Autowired
	ProductRepository productRepository;

	
	
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	
	public Product findById(Integer id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).get();
	}
	
	
	

	@Override
	public Page<Product> findProductByCategoryDetailFilter(int idCategoryDetail,
														   List<Integer> colorId,
														   List<Integer> sizeId,
														   Optional<String> sort,
														   Optional<Integer> p) {
		Sort s;
		if (sort.isEmpty() || sort.get().equals("price-asc")) {
			s = Sort.by(Sort.Direction.ASC, "price");
		} else if (sort.get().equals("price-desc")) {
			s = Sort.by(Sort.Direction.DESC, "price");
		} else if (sort.get().equals("name-az")) {
			s = Sort.by(Sort.Direction.ASC, "name");
		} else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}

		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		Pageable pageable = PageRequest.of(p.orElse(0), 12, s);
		return productRepository.findProductByCategoryDetailFilter(idCategoryDetail, listColorId, listSizeId, pageable);
	}	  

	@Override
	public Page<Product> findProductByCategoryFilter(int idCategory, List<Integer> colorId, List<Integer> sizeId, Optional<String> sort, Optional<Integer> p) {
		Sort s;
		if (sort.isEmpty() || sort.get().equals("price-asc")) {
			s = Sort.by(Sort.Direction.ASC, "price");
		} else if (sort.get().equals("price-desc")) {
			s = Sort.by(Sort.Direction.DESC, "price");
		} else if (sort.get().equals("name-az")) {
			s = Sort.by(Sort.Direction.ASC, "name");
		} else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}

		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		Pageable pageable = PageRequest.of(p.orElse(0), 12, s);
		return productRepository.findProductByCategoryFilter(idCategory, listColorId, listSizeId, pageable);
	}


	@Override
	public Product create(Product product) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Product> findALlProduct() {
		// TODO Auto-generated method stub
		return null;
	}


	

	

}
