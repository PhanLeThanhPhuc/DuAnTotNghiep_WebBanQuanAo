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

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Color;
import com.poly.elnr.service.ColorService;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/colors")
public class ColorRestController {

	@Autowired
	ColorService colorService;
	
	@GetMapping
	public List<Color> findAll() {
		return colorService.findAllColor();
	}
	
	@PostMapping
	public Color post(@RequestBody  Color create) {
		return colorService.create(create);
		 
	}
	@PutMapping("{id}")
	public Color put(@PathVariable("id") Integer id, @RequestBody Color color) {
		return colorService.update(color);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Color id) {
		colorService.delete(id);
	}
	
}
