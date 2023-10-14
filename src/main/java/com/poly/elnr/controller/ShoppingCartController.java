package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShoppingCartController {
	@RequestMapping("/cart")
	public String cart(Model model) {
		return "user/product/cart";
	}
}
