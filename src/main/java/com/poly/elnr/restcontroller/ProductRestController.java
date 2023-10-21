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
import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.CategoryService;
import com.poly.elnr.service.ProductDetailService;
import com.poly.elnr.service.ProductService;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping
	public List<Product> findAll() {
		return productService.findAll();
	}
	
	
	
	
}
