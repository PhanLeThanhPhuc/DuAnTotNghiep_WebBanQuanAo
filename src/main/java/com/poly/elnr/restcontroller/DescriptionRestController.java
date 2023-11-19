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

import com.poly.elnr.entity.Description;
import com.poly.elnr.service.DescriptionService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/description")
public class DescriptionRestController {
	
	@Autowired
	DescriptionService descriptionService;
	
	@GetMapping
	public List<Description> findAll() {
		return descriptionService.findAll();
	}
	
	@GetMapping("{id}")
	public Description getOne(@PathVariable("id") Integer id) {
		return descriptionService.findById(id);
	}
	
	@PostMapping
	public Description post(@RequestBody Description description) {
		descriptionService.create(description);
		return description;
	}
	@PutMapping("{id}")
	public Description put(@PathVariable("id") Integer id, @RequestBody Description description) {
		return descriptionService.update(description);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		descriptionService.delete(id);
	}
}
