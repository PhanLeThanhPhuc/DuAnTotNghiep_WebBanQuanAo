	package com.poly.elnr.controller;

import com.poly.elnr.service.CategoryDetailService;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.UserService;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.JMoleculesConverters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


	@Controller
public class UserController {

	@Autowired
	CategoryDetailService categoryDetailService;
	
	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}

	@GetMapping("user/order")
	public String userOrder(Model model){
		model.addAttribute("order", orderService.fillAllOrder());
		return "user/layout/user-order.html";
	}

	@GetMapping("user/order-detail")
	public String userOrderDetails(Model model, @RequestParam("idOrder") int idOrder){
		model.addAttribute("subTotal",orderService.subTotalOrder(idOrder));
		model.addAttribute("order", orderService.fillOrderById(idOrder));
		return "user/layout/user-orderdetails.html";
	}

}
