package com.poly.elnr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> fillAllOrder();

    Order fillOrderById(int id);

    double subTotalOrder(int idOrder);

    Order saveOrder(JsonNode orderData) throws JsonProcessingException;

}
