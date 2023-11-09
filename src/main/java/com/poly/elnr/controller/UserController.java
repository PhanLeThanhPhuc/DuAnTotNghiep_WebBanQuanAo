	package com.poly.elnr.controller;

import com.poly.elnr.dto.ChangePassword;
import com.poly.elnr.entity.Users;
import com.poly.elnr.service.CategoryDetailService;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.UserService;

import java.io.IOException;


import com.poly.elnr.utils.UploadCloudinaryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UserController {

	@Autowired
	CategoryDetailService categoryDetailService;
	
	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@GetMapping("user/index")
	public String index(Model model) {
		return "user/layout/home";
	}

	@GetMapping("user/order")
	public String userOrder(Model model, Authentication authentication){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		model.addAttribute("order", orderService.findOrderByIdUser(userDetails.getUsername()));
		System.out.println();
		return "user/layout/user-order.html";
	}

	@GetMapping("user/order-detail")
	public String userOrderDetails(Model model, @RequestParam("idOrder") int idOrder){
		model.addAttribute("subTotal",orderService.subTotalOrder(idOrder));
		model.addAttribute("order", orderService.fillOrderById(idOrder));
		return "user/layout/user-orderdetails.html";
	}

	@GetMapping("user/info")
	public String userInfo(Model model, @ModelAttribute("account") Users users, Authentication authentication){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Users user = userService.findByUserNameLogin(userDetails.getUsername());
		model.addAttribute("account", user);
		return "user/layout/user-info";
	}

	@ResponseBody
	@PostMapping("user/change-password")
	public ResponseEntity<String> updatePassword (@RequestBody ChangePassword changePassword,  Authentication authentication){

		String oldPassword = passwordEncoder.encode(changePassword.getOldPassword());

		if (authentication != null) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Users user = userService.findByUserNameLogin(userDetails.getUsername());
			if (passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
				userService.changePassword(oldPassword, userDetails.getUsername());
				return new ResponseEntity<>("{ \"message\": \"Cập nhật mật khẩu thành công.\" }", HttpStatus.OK);
			}else{
				return new ResponseEntity<>("{ \"error\": \"Mật khẩu cũ không đúng.\" }", HttpStatus.BAD_REQUEST);
			}
		}
        return null;
    }

	@ResponseBody
	@PostMapping("user/upload")
	public String uploadFile(@RequestParam("uploadfile") MultipartFile multipartFile,
							 Model model, Authentication authentication) throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.saveImageUser(multipartFile, userDetails.getUsername());
	}
}
