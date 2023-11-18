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

import com.poly.elnr.entity.Discount;
import com.poly.elnr.service.DiscountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/discount")
public class DiscountRestController {

	@Autowired
	DiscountService discountService;
	
	@GetMapping
	public List<Discount> findAll(){
		return discountService.findAll();
	}
	@GetMapping("{id}")
	public Discount getOne(@PathVariable("id") Integer id) {
		return discountService.findById(id);
	}
	@PostMapping
	public Discount post(@RequestBody Discount discount) {
		discountService.create(discount);
		return discount;
	}
	@PutMapping("{id}")
	public Discount put(@PathVariable("id") Integer id, @RequestBody Discount discount) {
		return discountService.update(discount);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		discountService.delete(id);
	}
}
