package com.poly.elnr.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.elnr.dto.OrderDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.poly.elnr.dto.PhoneTotalDTO;
import com.poly.elnr.dto.TotalWithUserOrderDTO;
import com.poly.elnr.entity.*;
import com.poly.elnr.repository.*;
//import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.service.UserService;
import com.poly.elnr.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.OrderService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    VoucherRepository voucherRepository;

    private final List<SseEmitter> emitters = new ArrayList<>();
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

        Voucher voucherId = null;
        if(order.getVoucher().getId() != 0){
            voucherId = new Voucher();
            order.getVoucher().getId();
            voucherId.setId(order.getVoucher().getId());
            Voucher voucher = voucherRepository.findById(order.getVoucher().getId()).get();
            int quantity =  voucher.getQuantity() - 1;
            voucher.setQuantity(quantity);
            voucherRepository.save(voucher);
        }

        if(user == null){
            user = new Users();
            user.setPhone(order.getPhone());
            user.setSignup(false);
            user.setPasswordReset(false);
            Users userSave = userRepository.save(user);

            Users userId = new Users();
            userId.setId(user.getId());
            order.setVoucher(voucherId);
            order.setUser(userId);
            orderRepository.save(order);
        }else{
            Users userId = new Users();
            userId.setId(user.getId());
            order.setUser(userId);
            order.setVoucher(voucherId);
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
//        sendOrderUpdate(order);
        return order;
    }


    public void addSseEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeSseEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void sendOrderUpdate(Order order) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("order-update").data(order));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
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
//        sendOrderUpdate(order);
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
        String expected_delivery_time = (String) data.get("expected_delivery_time");
        LocalDateTime dateTime = LocalDateTime.parse(expected_delivery_time, DateTimeFormatter.ISO_DATE_TIME);
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        order.setExpectedDeliveryTime(timestamp);
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
            user = userRepository.findByEmail(username);
            if(user.getPhone() ==  null){
                return null;
            }else{
                List<Order> order =  orderRepository.findOrderByIdUser(user.getId());
                System.out.println();
                return orderRepository.findOrderByIdUser(user.getId());
            }
        }

    }

    @Override
    public Order updatePayment(int idOrder, int payment) {
        Order order = orderRepository.findById(idOrder).get();
        order.setPayment(payment);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order updatePaymentAndStatusPayment(int idOrder, int statusPayment, int payment) {
        Order order = orderRepository.findById(idOrder).get();
        order.setPayment(payment);
        order.setStatusPayment(statusPayment);
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<OrderDTO> findAllTotal() {
        return orderRepository.findAllTotal();
    }

    @Override
    public List<PhoneTotalDTO> findPhoneTotalDTO() {
        return orderRepository.findPhoneTotalDTO();
    }

    @Override
    public List<TotalWithUserOrderDTO> findTotalByPhoneAndDateRange() {
        return orderRepository.findTotalByPhoneAndDateRange();
    }

    @Override
    public List<PhoneTotalDTO> findTop10PhoneTotalsByDateRange(String startDate, String endDate) throws ParseException {
        Date startDateConvert = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date endDateConvert = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        return orderRepository.findTop10PhoneTotalsByDateRange(startDateConvert,endDateConvert);
    }

    @Override
    public List<PhoneTotalDTO> findPhoneTotalsForToday() {
        return orderRepository.findTop10PhoneTotalsForToday();
    }

    @Override
    public Order updateStatusOrder(int id, int status) {
        Order order = orderRepository.findById(id).get();
        order.setStatus(status);
        orderRepository.save(order);
//        sendOrderUpdate(order);
        return order;
    }

    @Override
    public Order findOrderByPhoneAndId(String phone, int idOrder) {
        return orderRepository.findOrderByPhoneAndId(phone, idOrder);
    }

}