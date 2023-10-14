package com.poly.elnr.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.service.ProductService;



import com.poly.elnr.service.ProductService;

@Controller
@RequestMapping("user")
public class ProductController {

	@Autowired

	ProductRepository dao;

	public List<Product> findAll() {
		return dao.findAll();
	}
	
	public Product findById(Integer id) {
		return dao.findById(id).get();
	}

	/*
	 * public List<Product> findByCategoryId(String cid) { return
	 * dao.findByCategoryId(cid); }
	 */

	public Product create(Product product) {
		return dao.save(product);
	}

	public Product update(Product product) {
		return dao.save(product);
	}

	public void delete(Integer id) {
		dao.deleteById(id);

	ProductService productService;
	
	@GetMapping("product")
	public String viewProduct(Model model) {
		model.addAttribute("product", productService.findAllProduct());
		return "user/layout/product";

	}
	
}
