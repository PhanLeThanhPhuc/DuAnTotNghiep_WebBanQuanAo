package com.poly.elnr.controller;

import com.poly.elnr.service.CategoryDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


	@GetMapping("admin/index")
	public String viewAdmin(Model model) {

		return"redirect:/assets/admin/layout/index.html";
	}
	
	
	
}
