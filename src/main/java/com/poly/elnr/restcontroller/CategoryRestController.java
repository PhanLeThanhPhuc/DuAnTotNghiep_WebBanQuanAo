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

import com.poly.elnr.entity.Category;
import com.poly.elnr.service.CategoryService;





@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping
	public List<Category> findAll() {
		return categoryService.findAll();
	}
	
	@PostMapping
	public Category post(@RequestBody  Category create) {
		return categoryService.create(create);
		 
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		categoryService.delete(id);
	}
	

}
