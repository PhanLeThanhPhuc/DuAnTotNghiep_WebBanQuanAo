package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Product;

public interface ProductService {


public List<Product> findAll() ;
	
	public Product findById(Integer id) ;

	public List<Product> findByCategoryId(String cid) ;

	public Product create(Product product) ;

	public Product update(Product product) ;

	public void delete(Integer id) ;
	

	List<Product> findAllProduct();

}
