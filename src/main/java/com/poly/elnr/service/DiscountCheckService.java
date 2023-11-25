package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

public interface DiscountCheckService {
	 public List<Product> getAllDiscountProducts(List<Product> productList);
	 public Product DiscountProduct(Product product);
	public List<Product> getDiscountedProducts();
	 public ProductDetails getProductDetails(ProductDetails productDetails);
	List<Product> getDiscountProducts2(List<Product> productList);
	boolean isDiscountProduct(Product product); 
	 
}
