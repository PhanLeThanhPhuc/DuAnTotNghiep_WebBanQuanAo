package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

public interface DiscountCheckService {
	 public List<Product> getAllDiscountProducts(List<Product> productList);
	 public Product DiscountProduct(Product product);
	public List<Product> getDiscountedProducts();
	 public ProductDetails getProductDetails(ProductDetails productDetails);
	 Page<Product> getDiscountProducts2(List<Product> products, Optional<String> sort, Optional<Integer> p, double min, double max);
	boolean isDiscountProduct(Product product); 
	 
}
