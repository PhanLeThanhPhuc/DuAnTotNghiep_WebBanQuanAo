package com.poly.elnr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.entity.Order;

import java.util.Map;

public interface ApiGHNService {

    Map<String, Object> shipFee(Map<String, Object> objectProduct) throws JsonProcessingException;

    Map<String, Object> CreateOrderGHN(Order orderData) throws JsonProcessingException;

    Map<String, Object> cancelOrder(String idOrderGhn) throws JsonProcessingException;

}
