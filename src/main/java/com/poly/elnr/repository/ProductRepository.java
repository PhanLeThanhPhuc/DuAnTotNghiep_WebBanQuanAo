package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query( "SELECT p FROM Product p "
			+ " INNER JOIN p.productDetails pd "
			+ "WHERE p.id =?1 and pd.size.id =?2")
	Product findProductSize(Integer id, Integer sizeid);


	

}
	
