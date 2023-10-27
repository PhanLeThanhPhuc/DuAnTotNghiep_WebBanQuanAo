package com.poly.elnr.service;

import com.poly.elnr.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> fillAllOrder();

    Order fillOrderById(int id);

}
