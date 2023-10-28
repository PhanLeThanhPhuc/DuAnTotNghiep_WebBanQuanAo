package com.poly.elnr.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
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





	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE p.categoryDdetail.id =:idCategoryDetail "
			+ "AND (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId) ")
	Page<Product> findProductByCategoryDetailFilter(@Param("idCategoryDetail") int idCategoryDetail,
													@Param("colorId") List<Integer> colorId,
													@Param("sizeId") List<Integer> sizeId,
													Pageable pageable);

	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE p.categoryDdetail.category.id =:idCategory "
			+ "AND (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId) ")
	Page<Product> findProductByCategoryFilter(@Param("idCategory") int idCategoryDetail,
													@Param("colorId") List<Integer> colorId,
													@Param("sizeId") List<Integer> sizeId,
													Pageable pageable);
	
}
	
