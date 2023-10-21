package com.poly.elnr.service;

import java.util.List;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Product;

public interface ProductService {
	public List<Product> findAll();

	Product findById(Integer id);
	public Product create(Product product) ;
	public void delete(Integer id) ;

	List<Product> findALlProduct();
	Page<Product> findProductByCategoryDetailFilter(int idCategoryDetail,
													List<Integer> colorId,
													List<Integer> sizeId,
													Optional<String> sort,
													Optional<Integer> p);
	
	Page<Product> findProductByCategoryFilter(int idCategoryDetail,
													List<Integer> colorId,
													List<Integer> sizeId,
													Optional<String> sort,
													Optional<Integer> p);
}
