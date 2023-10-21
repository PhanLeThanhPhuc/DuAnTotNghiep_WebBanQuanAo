package com.poly.elnr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.elnr.security.CustomUserDetails;
import com.poly.elnr.service.serviceImpl.SessionService;


@Controller
public class SecurityController {

	@GetMapping("/security/user")
	public String loginForm(Model model) {
		return "user/security/login";
	}
}
