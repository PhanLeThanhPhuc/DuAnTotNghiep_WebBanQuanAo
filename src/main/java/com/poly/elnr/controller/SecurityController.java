package com.poly.elnr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.elnr.config.UserInfoUserDetails;
import com.poly.elnr.service.SessionService;


@Controller
public class SecurityController {
		
	
	@Autowired
	SessionService session;
	
	@Autowired
	UserInfoUserDetails userInfoUserDetails;
	
//	@GetMapping("/user/index")
//	public String index(Model model) {
////		System.out.println("TEst: "+userInfoUserDetails.getName());
////		System.out.println("fjdahjfd "+session.get("email"));
//		return "user/layout/home";
//	}

	@GetMapping("/security/user")
	public String loginForm(Model model) {
		return "user/security/login";
		
	}
}
