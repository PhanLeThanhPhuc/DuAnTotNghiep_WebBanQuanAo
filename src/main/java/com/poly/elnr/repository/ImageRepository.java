package com.poly.elnr.repository;

import java.awt.image.ImageProducer;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.ImageProduct;

@Repository
public interface ImageRepository extends JpaRepository<ImageProduct, Integer> {
	@Query("select p from ImageProduct p where p.product.id=?1")
	List<ImageProduct> findByProductID(Integer id);


	@Transactional
	@Modifying
	@Query("DELETE FROM ImageProduct ip WHERE ip.product.id = :idProduct")
	void deleteByProductId(@Param("idProduct") int idProduct);


}
