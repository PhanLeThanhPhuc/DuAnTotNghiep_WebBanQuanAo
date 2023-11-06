package com.poly.elnr.service.serviceImpl;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.Review;
import com.poly.elnr.repository.ReviewRepository;
import com.poly.elnr.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService{
	
	@Autowired
	ReviewRepository reviewRepository;

	@Override
	public Page<Review> findByProductID(Integer id, Optional<Integer> p) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		return reviewRepository.findByProductID(id,pageable);
	}

	@Override
	public List<Review> findByProductID2(Integer id) {
		// TODO Auto-generated method stub
		return reviewRepository.findByProductID2(id);
	}

	
	public List<Review> findAll() {
		// TODO Auto-generated method stub
		return reviewRepository.findAll();
	}

	
	public Review create(Review create) {
		// TODO Auto-generated method stub
		return reviewRepository.save(create);
	}

	public Review update(Review review) {
		// TODO Auto-generated method stub
		return reviewRepository.save(review);
	}

	
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		reviewRepository.deleteById(id);
	}

	
	

	

}
