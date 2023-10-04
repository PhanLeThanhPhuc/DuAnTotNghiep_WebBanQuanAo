package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {
	
	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}

}
