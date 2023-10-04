package com.poly.elnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("admin/index")
	public String viewAdmin() {
		return"redirect:/assets/admin/layout/index.html";
	}
	
}
