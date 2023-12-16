	package com.poly.elnr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.poly.elnr.dto.ChangePassword;
import com.poly.elnr.dto.UserRegisterDTO;
import com.poly.elnr.entity.Order;
import com.poly.elnr.entity.Users;

import com.poly.elnr.service.CategoryDetailService;
import com.poly.elnr.service.DiscountCheckService;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.service.UserService;

import com.poly.elnr.service.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import com.poly.elnr.utils.DiscountCheck;
import com.poly.elnr.utils.UploadCloudinaryUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
	PasswordEncoder passwordEncoder;

	@Autowired
	VnPayService vnPayService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	DiscountCheckService discountCheckService;

	@Autowired
	DiscountCheck discountCheck;

	@Autowired
	AddressService addressService;

	@Autowired
	ProductService productService;
	
	
	@PostMapping("/submitPayment/{paymentMethod}/{idOrder}")
	public String submitPayment(@PathVariable int paymentMethod, @PathVariable int idOrder) {

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		if(paymentMethod == 0){
			System.out.println("tien mat");
			orderService.updatePayment(idOrder,paymentMethod);
			return "redirect:/user/information-order?idorder="+idOrder;
		}else{
			baseUrl += "/user/payment-order-user";
			String content = "Thanh toán đơn hàng ";
			Order order = orderService.fillOrderById(idOrder);
			int total = order.getTotal() -  order.getTotalDiscount() + order.getShipFee();
//			if(order.getVoucher() == null){
//				total = (int) ((order.getTotal() + order.getShipFee()));
//			}else{
//				total = (int) ((order.getTotal() + order.getShipFee()) - order.getVoucher().getDiscountPrice());
//			}
			String urlPayment = vnPayService.createOrder(total, content, baseUrl,idOrder);
			return "redirect:"+urlPayment;
		}
	}

	@GetMapping("user/index")
	public String index(Model model) {
		model.addAttribute("sale",discountCheckService.getDiscountedProducts());
		model.addAttribute("products",productService.findAll());
		return "user/layout/home";
	}

	@GetMapping("user/order")
	public String userOrder(Model model, Authentication authentication){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		model.addAttribute("order", orderService.findOrderByIdUser(userDetails.getUsername()));
		return "user/layout/user-order.html";
	}


	@GetMapping("user/payment-order-user")
	public String paymentOrderUser(){
		//0 fail, 1 success
		int paymentStatus =vnPayService.orderReturn(request);
		int idorder = Integer.parseInt(request.getParameter("vnp_TxnRef"));
		System.out.println("THời gian thanh toán: "+ request.getParameter("vnp_PayDate"));
		Order order = new Order();

		if(paymentStatus==1){
			int payment = 1;
			int statusPayment = 1;
			order= orderService.updatePaymentAndStatusPayment(idorder,statusPayment, payment);
		}else{
			int payment = 1;
			int statusPayment = 0;
			order= orderService.updatePaymentAndStatusPayment(idorder,statusPayment, payment);
		}
		return "redirect:/user/information-order?idorder="+idorder;
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

//		String oldPassword = passwordEncoder.encode(changePassword.getOldPassword());
		String newPassword = passwordEncoder.encode(changePassword.getNewPassword());

		if (authentication != null) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Users user = userService.findByUserNameLogin(userDetails.getUsername());
			if (passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
					userService.changePassword(newPassword, userDetails.getUsername());
				return new ResponseEntity<>("{ \"message\": \"Cập nhật mật khẩu thành công.\" }", HttpStatus.OK);
			}else{
				return new ResponseEntity<>("{ \"error\": \"Mật khẩu cũ không đúng.\" }", HttpStatus.BAD_REQUEST);
			}
		}
        return null;
    }

	@GetMapping("user/cancel-order")
	public String cancelOrder (@RequestParam("idorder") int idorder) throws JsonProcessingException {
		orderService.cancelOrder(idorder);
		return "redirect:/user/order-detail?idOrder="+idorder;
	}

	@ResponseBody
	@PostMapping("user/upload")
	public String uploadFile(@RequestParam("uploadfile") MultipartFile multipartFile,
							 Model model, Authentication authentication) throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.saveImageUser(multipartFile, userDetails.getUsername());
	}

	@GetMapping("user/register-form")
	public String userRegisterForm(){
//		model.addAttribute("")
		return "user/security/register";
	}

	@GetMapping("/user/login")
	public String loginForm(Model model) {
		return "user/security/login";
	}

	@PostMapping("user/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userRegisterDTO){
		Users userEmail = userService.findByEmail(userRegisterDTO.getEmail());
		Users userPhone = userService.findByPhone(userRegisterDTO.getPhone());
		Map<String, Object> map = new HashMap<>();
		if (userEmail != null) {
				map.put("status", false);
				map.put("message", "Email đã được sử dụng");
				return ResponseEntity.ok(map);
		} else if (userPhone != null && userPhone.isSignup() == true) {
				map.put("status", false);
				map.put("message", "Phone đã được sử dụng");
				return ResponseEntity.ok(map);
		} else if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
			map.put("status", false);
			map.put("message", "Mật khẩu không khớp");
			return ResponseEntity.ok(map);
		} else {
			userService.registerUser(userRegisterDTO);
			map.put("status", true);
			map.put("message", "Đăng ký thành công");
			return ResponseEntity.ok(map);
		}
	}

	@GetMapping("user/address")
	public String viewAddress (Model model, Authentication authentication){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		model.addAttribute("address", addressService.findAddressByIdUser(userDetails.getUsername()));
		return "user/layout/user-address";
	}
	
	@GetMapping("user/blog")
	public String viewBlog (Model model, Authentication authentication){
		return "user/layout/blog";
	}

}
