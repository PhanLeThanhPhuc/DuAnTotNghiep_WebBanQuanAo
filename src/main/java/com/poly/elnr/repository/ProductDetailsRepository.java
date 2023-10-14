package com.poly.elnr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {

	@Query("SELECT o FROM ProductDetails o WHERE o.product.id like ?1")
	List<ProductDetails> findByProductID(Integer id);

	@Query("SELECT o FROM ProductDetails o WHERE o.product.id like ?1 and o.size.id like ?2")
	ProductDetails findProductDetial(Integer id, Integer sizeid);

}
