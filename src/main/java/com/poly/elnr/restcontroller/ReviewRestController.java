package com.poly.elnr.restcontroller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.Review;
import com.poly.elnr.service.ReviewService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/review")
public class ReviewRestController {
	
	@Autowired
	ReviewService reviewService;
	
	@GetMapping
	public List<Review> findAll() {
		return reviewService.findAll();
	}
	
	@GetMapping("product/{id}")
	public List<Review> findByProductID(@PathVariable("id") Integer id) {
		return reviewService.findByProductID2(id);
	}
	
	@PostMapping
	public Review post(@RequestBody  Review create) {
		return reviewService.create(create);
		 
	}
	@PutMapping("{id}")
	public Review put(@PathVariable("id") Integer id, @RequestBody Review review) {
		return reviewService.update(review);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		reviewService.delete(id);
	}
}
