package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class CartController {
	@RequestMapping("/cart")
	public String viewcart() {
		return"user/product/cart-view";
	}
	
	
	@RequestMapping("cart-info")
	public String viewcartinfor() {
		return"user/layout/cart-info";
	}
	
}
