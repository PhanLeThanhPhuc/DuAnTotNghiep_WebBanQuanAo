package com.poly.elnr.service.serviceImpl;

import com.poly.elnr.entity.Order;
import com.poly.elnr.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;


    @Override
    public List<Order> fillAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order fillOrderById(int id) {
        return orderRepository.findById(id).get();
    }
}
