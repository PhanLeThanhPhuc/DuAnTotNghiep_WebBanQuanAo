package com.poly.elnr.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VnPayService {

	public String createOrder(int total, String orderInfor, String urlReturn, int orderId);
	
	public int orderReturn(HttpServletRequest request);
}
