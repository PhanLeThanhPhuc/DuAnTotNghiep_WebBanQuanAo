package com.poly.elnr.service;

import com.poly.elnr.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public interface VnPayService {

	public String createOrder(int total, String orderInfor, String urlReturn, int orderId);
	
	public int orderReturn(HttpServletRequest request);

	public String refundVnPay(Order order) throws IOException;
}
