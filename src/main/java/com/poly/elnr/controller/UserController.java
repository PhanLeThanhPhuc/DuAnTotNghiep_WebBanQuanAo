package com.poly.elnr.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.elnr.repository.UserRepository;

//@RequestMapping("user")dev
@Controller
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("user")
	public String index() {
		userRepository.findAll().forEach(list->{
			System.out.println("Name: "+list.getFullName());
		});
		return "user/index";
	}
	
}
