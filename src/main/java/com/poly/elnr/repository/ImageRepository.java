package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.ImageProduct;

@Repository
public interface ImageRepository extends JpaRepository<ImageProduct, Integer> {

}
