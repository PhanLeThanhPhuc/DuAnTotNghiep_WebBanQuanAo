package com.poly.elnr.restcontroller;

import java.util.Date;
import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.DiscountCheckService;
import com.poly.elnr.service.DiscountService;
import com.poly.elnr.service.ProductDetailService;
import com.poly.elnr.utils.DiscountCheck;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/productsDetail")
public class ProductDetailRestController {
	
	@Autowired
	ProductDetailService detailService;
	
	@Autowired
	DiscountCheckService discountCheckService;
	@Autowired 
	DiscountCheck discountCheck;
	@GetMapping("{id}")
	public List<ProductDetails> getProductDetail(@PathVariable("id") Integer id) {
		return detailService.findByProductID(id);
	}
	
	@GetMapping("date")
	public Date getDate() {
		return discountCheck.getDateEnd(new Date());
	}
	
	
	@PostMapping
	public ProductDetails post(@RequestBody ProductDetails productdetail) {
		detailService.create(productdetail);
		return productdetail;
	}
	@PutMapping("{id}")
	public ProductDetails put(@PathVariable("id") Integer id, @RequestBody ProductDetails productdetail) {
		return detailService.update(productdetail);
	}

	@GetMapping(value="size/{id}/{sizeid}" )
	public ProductDetails getProductSize(@PathVariable("id") Integer id,@PathVariable("sizeid") Integer sizeid) {
		return discountCheckService.getProductDetails(detailService.findProductDetial(id,sizeid));
	}
}
