package com.poly.elnr.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	@GetMapping("{id}")
	public List<CategoryDetail> findAllByCategoryID(@PathVariable("id") Integer id ) {
		return categorydetailService.findByCategoryID(id);
	}
	
	@PostMapping
	public CategoryDetail post(@RequestBody  CategoryDetail create) {
		return categorydetailService.create(create);
		 
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") CategoryDetail id) {
		categorydetailService.delete(id);
	}

	@PutMapping("{id}")
	public CategoryDetail update(@PathVariable("id") Integer id, @RequestBody CategoryDetail categoryDetail) {
		return categorydetailService.update(categoryDetail);
	}

}
