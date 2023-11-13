package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import com.poly.elnr.entity.Product;

public interface DiscountCheckService {
	 public List<Product> getAllDiscountProducts(List<Product> productList);
	 public Optional<Product> getDiscountProductById(int id) ;
	 public Product saveDiscountProduct(Product product);
	 
}
