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

import com.poly.elnr.entity.ImageProduct;
import com.poly.elnr.service.ImageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/image")
public class ProductImageRestController {
	@Autowired
	ImageService imageService;
	
	@GetMapping
	public List<ImageProduct> getAll(){
		return imageService.findAll();
	}
	
	@GetMapping("{id}")
	public List<ImageProduct> getOne(@PathVariable("id") Integer id) {
		return imageService.findByProductId(id);
	}
	
	@PostMapping
	public ImageProduct post(@RequestBody ImageProduct image) {
		imageService.create(image);
		return image;
	}
	@PutMapping("{id}")
	public ImageProduct put(@PathVariable("id") Integer id, @RequestBody ImageProduct image) {
		return imageService.update(image);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		imageService.delete(id);
	}
}