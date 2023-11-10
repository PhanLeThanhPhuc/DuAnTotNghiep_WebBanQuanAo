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
import com.poly.elnr.repository.ProductDetailsRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.service.UserService;
import com.poly.elnr.utils.RegexUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.OrderService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    ApiGHNService apiGHNService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductDetailsRepository productDetailsRepository;


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
            subTotal += orderDetail.getPrice() * orderDetail.getQuantity();
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
        //save orderdetail
        TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
        List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type)
                .stream().peek(d -> d.setOrder(order)).collect(Collectors.toList());
        orderDetailRepository.saveAll(details);

        ///tru sl trong product
        order.getOrderDetails().forEach(orderDetail ->{
            ProductDetails productDetails = productDetailsRepository.findById(orderDetail.getProductDetails().getId()).get();
            int qty = productDetails.getQuantity() - orderDetail.getQuantity();
            productDetails.setQuantity(qty);
            productDetailsRepository.save(productDetails);
        });

        return order;
    }

    @Override
    public Order cancelOrder(int orderId) throws JsonProcessingException {
        Order order =  orderRepository.findById(orderId).get();
        if(Objects.equals(order.getShipCode(), "")){
            order.setStatus(2);
        }else{
            apiGHNService.cancelOrder(order.getShipCode());
            order.setStatus(2);
        }
        //+ lai sl product
        order.getOrderDetails().forEach(orderDetail ->{
            ProductDetails productDetails = productDetailsRepository.findById(orderDetail.getProductDetails().getId()).get();
            int qty = productDetails.getQuantity() + orderDetail.getQuantity();
            productDetails.setQuantity(qty);
            productDetailsRepository.save(productDetails);
        });
        return orderRepository.save(order);
    }

    @Override
    public Order updateStatusPayment(int idOrder, int statusPayment) {
        Order order = orderRepository.findById(idOrder).get();
        order.setStatusPayment(statusPayment);
        return orderRepository.save(order);
    }

    @Override
    public Order orderGhn(int orderId) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId).get();

        Map<String, Object> responeCreateOrder =  apiGHNService.CreateOrderGHN(order);

        Map<String, Object> data = (Map<String, Object>) responeCreateOrder.get("data");
        String idOrderGhn = (String) data.get("order_code");
        order.setShipCode(idOrderGhn);
        order.setStatus(1);
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findOrderByIdUser(String username) {
        Users user = new Users();
        if(RegexUtils.isPhoneNumber(username)) {
            user = userRepository.findByPhone(username);
            return orderRepository.findOrderByIdUser(user.getId());
        }else{
            user = userRepository.findEmail(username);
           if(user.getPhone() ==  null){
               return null;
           }else{
               List<Order> order =  orderRepository.findOrderByIdUser(user.getId());
               System.out.println();
               return orderRepository.findOrderByIdUser(user.getId());
           }
        }

    }

}
