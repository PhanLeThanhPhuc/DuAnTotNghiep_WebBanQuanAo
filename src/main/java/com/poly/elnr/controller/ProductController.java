package com.poly.elnr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.ProductDetailService;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.service.ProductService;



@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	@Autowired
	ProductDetailService detailService;
	
	
	@RequestMapping("/product/list/form")
	public String product(Model model){
			List<Product> list = productService.findAll();
			model.addAttribute("items", list);
		return "user/product/list";
				
	}
	
	@RequestMapping("/product/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		List<ProductDetails> size = detailService.findByProductID(id);
		model.addAttribute("sizes", size);
		Product item = productService.findById(id)	;
		model.addAttribute("item", item);
		return "user/product/detail";
	}
	
	
	
		
}