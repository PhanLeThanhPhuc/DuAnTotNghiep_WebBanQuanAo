package com.poly.elnr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.poly.elnr.dto.OrderData;

import java.util.Map;

public interface ApiGHNService {

    Map<String, Object> shipFee(Map<String, Object> objectProduct) throws JsonProcessingException;

//    Map<String, Object> CreateOrderGHN(OrderData orderData) throws JsonProcessingException;

}
