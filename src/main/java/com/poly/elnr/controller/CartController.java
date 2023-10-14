package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {
	@RequestMapping("/cart/view")
	public String viewAdmin() {
		return"user/product/cart-view";
	}
	
}
