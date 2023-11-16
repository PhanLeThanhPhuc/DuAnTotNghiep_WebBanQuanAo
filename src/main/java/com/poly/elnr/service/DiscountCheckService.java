package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

public interface DiscountCheckService {
	 public List<Product> getAllDiscountProducts(List<Product> productList);
	 public Product saveDiscountProduct(Product product);
	public List<Product> getDiscountedProducts();
	 public ProductDetails getProductDetails(ProductDetails productDetails); 
	 
}
