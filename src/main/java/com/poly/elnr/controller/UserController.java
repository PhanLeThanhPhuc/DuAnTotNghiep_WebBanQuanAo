	package com.poly.elnr.controller;

import com.poly.elnr.service.CategoryDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

	@Autowired
	CategoryDetailService categoryDetailService;

	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}
	

}
