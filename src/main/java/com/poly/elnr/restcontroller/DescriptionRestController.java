package com.poly.elnr.restcontroller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
