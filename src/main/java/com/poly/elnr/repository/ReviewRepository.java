package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

}
