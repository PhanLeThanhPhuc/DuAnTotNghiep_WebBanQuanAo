package com.poly.elnr.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VnPayService {

	public String createOrder(int total, String orderInfor, String urlReturn);
	
	public int orderReturn(HttpServletRequest request);
}
