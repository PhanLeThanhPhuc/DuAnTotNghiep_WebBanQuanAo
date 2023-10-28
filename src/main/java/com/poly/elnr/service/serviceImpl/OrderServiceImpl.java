package com.poly.elnr.service.serviceImpl;

import com.poly.elnr.entity.DiscountDetail;
import com.poly.elnr.entity.Order;
import com.poly.elnr.entity.OrderDetail;
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

    @Override
    public double subTotalOrder(int idOrder) {

        Order order = orderRepository.findById(idOrder).get();
        double subTotal = 0;

        for (OrderDetail orderDetail : order.getOrderDetails()){
            if(orderDetail.getProductDetails().getProduct().getDiscountDetail().isEmpty()){
                System.out.println("K có khuyến mãi");
                subTotal += orderDetail.getProductDetails().getProduct().getPrice() * orderDetail.getQuantity();
            }else {
                System.out.println("Có khuyến mãi");
               for (DiscountDetail discountDetail : orderDetail.getProductDetails().getProduct().getDiscountDetail()){
                   subTotal += discountDetail.getDiscount().getDiscount() * orderDetail.getQuantity();
               }
            }
        }
        return subTotal;
    }
}
