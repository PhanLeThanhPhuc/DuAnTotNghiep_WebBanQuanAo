package com.poly.elnr.restcontroller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.ProductDetailService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/productsDetail")
public class ProductDetailRestController {
	
	@Autowired
	ProductDetailService detailService;
	
	
	@GetMapping("{id}")
	public List<ProductDetails> getProductDetail(@PathVariable("id") Integer id) {
		return detailService.findByProductID(id);
	}
	

	@GetMapping(value="size/{id}/{sizeid}" )
	public ProductDetails getProductSize(@PathVariable("id") Integer id,@PathVariable("sizeid") Integer sizeid) {
		return detailService.findProductDetial(id,sizeid);
	}
}
