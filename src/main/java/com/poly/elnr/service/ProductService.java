package com.poly.elnr.service;

public interface ProductService {

<<<<<<< Updated upstream
=======
public List<Product> findAll() ;
	
	public Product findById(Integer id) ;

	public List<Product> findByCategoryId(String cid) ;

	public Product create(Product product) ;

	public Product update(Product product) ;

	public void delete(Integer id) ;
	
	
>>>>>>> Stashed changes
}
