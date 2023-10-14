package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.poly.elnr.entity.Product;

public interface ProductService {
	
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
