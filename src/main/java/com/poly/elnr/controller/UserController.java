package com.poly.elnr.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.elnr.repository.CategoryDetailRepository;
import com.poly.elnr.repository.CategoryRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.CategoryDetailService;

@Controller
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CategoryDetailService categoryDetailService;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("user/index")
	public String index() {
		categoryDetailService.findCategoryAndCategoryDetail().forEach(list ->{
			System.out.println(list.getCatgoryName());
			System.out.println(list.getIdCategory());
			System.out.println(list.getCatgoryDetailName());
		});
		return "user/layout/home";
	}

}
