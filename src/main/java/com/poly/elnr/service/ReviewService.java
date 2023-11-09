package com.poly.elnr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.Review;

public interface ReviewService {

	Page<Review> findByProductID(Integer id, Optional<Integer> p);

	List<Review> findByProductID2(Integer id);

	List<Review> findAll();

	Review create(Review create);

	Review update(Review review);

	void delete(Integer id);

	




}
