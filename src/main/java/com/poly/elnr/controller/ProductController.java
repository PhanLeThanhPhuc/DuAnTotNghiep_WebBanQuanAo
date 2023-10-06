package com.poly.elnr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.service.ProductService;

@Controller
@RequestMapping("user")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("product")
	public String viewProduct(Model model) {
		model.addAttribute("product", productService.findAllProduct());
		return "user/layout/product";
	}
	
}
