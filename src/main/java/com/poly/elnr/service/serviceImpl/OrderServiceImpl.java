package com.poly.elnr.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.elnr.dto.OrderData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.poly.elnr.entity.*;
import com.poly.elnr.repository.OrderDetailRepository;
import com.poly.elnr.repository.OrderRepository;
//import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.OrderService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    OrderDetailRepository orderDetailRepository;


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
            if(orderDetail.getDiscountPrice() == 0){
                subTotal += orderDetail.getPrice() * orderDetail.getQuantity();
            }else{
                subTotal += orderDetail.getDiscountPrice() * orderDetail.getQuantity();
            }
        }
        return subTotal;
    }

    @Override
    public Order saveOrder(JsonNode orderData) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        Order order = mapper.convertValue(orderData, Order.class);

        Users user = userService.findByUserNamePhoneAndEmail(order.getPhone());

        if(user == null){
           user = new Users();
           user.setPhone(order.getPhone());
           user.setSignup(false);
           user.setPasswordReset(false);
           Users userSave = userRepository.save(user);

            Users userId = new Users();
            userId.setId(user.getId());

            order.setUser(userId);
            orderRepository.save(order);
        }else{
            Users userId = new Users();
            userId.setId(user.getId());
            order.setUser(userId);
            orderRepository.save(order);
        }
        TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
        List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type)
                .stream().peek(d -> d.setOrder(order)).collect(Collectors.toList());
        orderDetailRepository.saveAll(details);
        return order;
    }


//    public Order dataOrder (OrderData orderData){
//        Order orderEntity = new Order();
//        OrderData.Order order = orderData.getOrder();
//
//        orderEntity.setOrderDate(order.getOrderDate());
//        orderEntity.setWard(order.getWard());
//        orderEntity.setDistrict(order.getDistrict());
//        orderEntity.setProvince(order.getProvince());
//        orderEntity.setPayment(order.getPayment());
//        orderEntity.setTotal(order.getTotal());
//        orderEntity.setEmail(order.getEmail());
//        orderEntity.setTotalDiscount(order.getTotalDiscount());
//        orderEntity.setDistrictId(orderData.getOrderDataGHN().getTo_district_id());
//        orderEntity.setWardCode(orderData.getOrderDataGHN().getTo_ward_code());
//        orderEntity.setWeight(orderData.getOrderDataGHN().getWeight());
//        orderEntity.setNameUser(orderData.getOrderDataGHN().getTo_name());
//        orderEntity.setDetailAddress(order.getDetailAddress());
//        return orderEntity;
//    }
//
//    public void saveOrderDetails(Order order,OrderData orderData){
//        orderData.getOrder().getOrderDetails().forEach(od ->{
//            int idOrder = order.getId();
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setPrice(od.getPrice());
//            orderDetail.setDiscountPrice(od.getDiscountPrice());
//            orderDetail.setQuantity(od.getQuantity());
//            ProductDetails idProductDetails = new ProductDetails();
//            orderDetail.setId(od.getProductDetails().getId());
//            Order orderId =  new Order();
//            orderId.setId(idOrder);
//            orderDetail.setOrder(orderId);
//            orderDetailRepository.save(orderDetail);
//        });
//    }

}
