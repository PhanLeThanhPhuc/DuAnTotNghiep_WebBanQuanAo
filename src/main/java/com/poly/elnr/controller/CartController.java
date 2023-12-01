package com.poly.elnr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.entity.Order;
import com.poly.elnr.service.ApiAddressService;
import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.VnPayService;
import com.poly.elnr.utils.ApiGetAddress;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
//@RequestMapping("/user")
public class CartController {

	@Autowired
	VnPayService vnPayService;

	@Autowired
	ApiAddressService apiAddressService;

	@Autowired
	ApiGHNService apiGHNService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	OrderService orderService;



	@RequestMapping("/user/cart")
	public String viewcart() {
		return"user/product/cart-view";
	}
	
	
	@RequestMapping("/user/cart-info")
	public String viewcartinfor() {
		return"user/layout/cart-info";
	}

	@GetMapping("/user/checkout")
	public String checkout(){
		return "user/product/checkout";
	}

	@ResponseBody
	@GetMapping("/user/province")
	public ResponseEntity<?> fillAllProvince(){
		return ResponseEntity.ok(apiAddressService.listProvinces()) ;
	}

	@ResponseBody
	@GetMapping("/user/district")
	public String fillAllDistrict(@RequestParam("province_id") int province_id){
		return apiAddressService.listDistrict(province_id);
	}

	@ResponseBody
	@GetMapping("/user/ward")
	public String fillAllWard(@RequestParam("district_id") int district_id){
		return apiAddressService.listWard(district_id);
	}

	@ResponseBody
	@PostMapping("/user/shipfee")
	public Map<String, Object> shipFee(@RequestBody Map<String, Object> test) throws JsonProcessingException {

        return apiGHNService.shipFee(test);
    }



}
