package com.poly.elnr.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.poly.elnr.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p from Product p where p.categoryDdetail.name =:name")
	Page<Product> findProductByCategoryDetail(@Param("name") String name, Pageable pageable);

	@Query("SELECT p FROM Product p "
            + "INNER JOIN p.productDetails pd "
            + "WHERE p.categoryDdetail.name =:name "
            + "AND (p.color.id IN :colorId "
            + "OR pd.size.id IN :sizeId) ")
	Page<Product> findProductByCategoryDetailFilter(@Param("name") String name,
													@Param("colorId") List<Integer> colorId,
													@Param("sizeId") List<Integer> sizeId,
													Pageable pageable);
	
//	@Query("SELECT p FROM Product p "
//			  + "INNER JOIN p.productDetails pd where p.categoryDdetail.name =:name "
//			  + "AND p.color.id IN :colorId "
//			  + "AND pd.size.id IN :sizeId ")
//		Page<Product> findProductByCategoryDetailFilterNull(@Param("name") String name,
//														@Param("colorId") List<Integer> colorId,
//														@Param("sizeId") List<Integer> sizeId,
//														Pageable pageable);
//	
	
}
