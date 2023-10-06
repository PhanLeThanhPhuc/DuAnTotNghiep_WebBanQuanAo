package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.poly.elnr.entity.Product;

public interface ProductService {

	Page<Product> findProductByCategoryDetail(String name,
											  Optional<Integer> p);
	
	Page<Product> findProductByCategoryDetailFilter(String name,
													List<Integer> colorId, 
													List<Integer> sizeId,
													Optional<String> sort,
													Optional<Integer> p);
	
}
