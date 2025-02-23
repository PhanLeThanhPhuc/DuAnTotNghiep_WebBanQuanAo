package com.poly.elnr.repository;

import java.util.List;


import org.springframework.data.jpa.repository.*;
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
			+ "WHERE p.id =?1 and pd.size.id =?2 and pd.size.status=true")
	Product findProductSize(Integer id, Integer sizeid);

	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE p.categoryDdetail.id =:idCategoryDetail "
			+ "AND (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId)  and p.status=true "
			+ "AND  (CASE WHEN p.isSale = false THEN p.price ELSE p.discountPrice END)  BETWEEN :min AND :max" )
	Page<Product> findProductByCategoryDetailFilter(@Param("idCategoryDetail") int idCategoryDetail,
													@Param("colorId") List<Integer> colorId,
													@Param("sizeId") List<Integer> sizeId,
													Pageable pageable,@Param("min") String min ,@Param("max") String max );

	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE p.categoryDdetail.category.id =:idCategory "
			+ "AND (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId)   and p.status=true "
			+ "and (CASE WHEN p.isSale = false THEN p.price ELSE p.discountPrice END) between :min AND :max"
			)
	Page<Product> findProductByCategoryFilter(@Param("idCategory") int idCategory,
												@Param("colorId") List<Integer> colorId,
												@Param("sizeId") List<Integer> sizeId,
												Pageable pageable,@Param("min") String min ,@Param("max") String max );

	@EntityGraph(attributePaths = {"categoryDdetail", "color", "description"})
	@Query("SELECT p FROM Product p where p.id IN :IdProduct")
	List<Product> findByIdsProduct(@Param("IdProduct") int[] IdProduct);



	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE  (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId)   and p.status=true")
	List<Product> findProductSale(@Param("colorId") List<Integer> colorId,
							@Param("sizeId") List<Integer> sizeId);
	@Query("SELECT DISTINCT p FROM Product p "
			+ "INNER JOIN p.productDetails pd "
			+ "WHERE  (p.color.id IN :colorId "
			+ "AND pd.size.id IN :sizeId)   and p.status=true and p.name LIKE :search "
			+ "and (CASE WHEN p.isSale = false THEN p.price ELSE p.discountPrice END) between :min AND :max")
	Page<Product> findProductSearch(@Param("colorId") List<Integer> colorId,
							@Param("sizeId") List<Integer> sizeId,Pageable pageable ,@Param("search") String search
							,@Param("min") String min ,@Param("max") String max);
	

}
	
