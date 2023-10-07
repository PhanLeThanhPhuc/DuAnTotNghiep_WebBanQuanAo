package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {
	
	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}
	
	@RequestMapping("/security/login/form")
	public String loginForm(Model model) {
		return "user/security/login";
	}

	@RequestMapping("/product")
	public String productlist(Model model) {
		return "user/product/list";
	}

}
