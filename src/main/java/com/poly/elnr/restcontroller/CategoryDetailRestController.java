package com.poly.elnr.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.CategoryDetail;
import com.poly.elnr.service.CategoryDetailService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categorydetail")
public class CategoryDetailRestController {
	
	
	@Autowired
	CategoryDetailService categorydetailService;
	
	@GetMapping
	public List<CategoryDetail> findAll() {
		return categorydetailService.findAll();
	}
	
	@PostMapping
	public CategoryDetail post(@RequestBody  CategoryDetail create) {
		return categorydetailService.create(create);
		 
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") CategoryDetail id) {
		categorydetailService.delete(id);
	}

}
