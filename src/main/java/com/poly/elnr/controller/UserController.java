	package com.poly.elnr.controller;

import com.poly.elnr.service.CategoryDetailService;
import com.poly.elnr.service.UserService;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

	@Autowired
	CategoryDetailService categoryDetailService;
	
	@Autowired
	UserService userService;

	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}

}
