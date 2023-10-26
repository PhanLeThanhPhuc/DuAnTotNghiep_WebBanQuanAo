package com.poly.elnr.repository;

import java.awt.image.ImageProducer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.ImageProduct;

@Repository
public interface ImageRepository extends JpaRepository<ImageProduct, Integer> {
	@Query("select p from ImageProduct p where p.product.id=?1")
	List<ImageProduct> findByProductID(Integer id);

}
