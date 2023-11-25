package com.poly.elnr.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.poly.elnr.dto.OrderData;
//import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.entity.Order;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderRestController {

    @Autowired
    VnPayService vnPayService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    OrderService orderService;


    @PostMapping("/user/order")
    public Map<String, Object> order (@RequestBody JsonNode orderData, HttpServletResponse response) throws IOException {
        int payment = Integer.parseInt(orderData.get("payment").asText());
        Order order = orderService.saveOrder(orderData);
            if(payment == 1){
                Map<String, Object> model = new HashMap<>();
                model.put("payment", payment);
                model.put("urlVnPay", vnPay(order));
                model.put("order", order);
                return model;
            }else{
                Map<String, Object> model = new HashMap<>();
                model.put("order", order);
                return model;
            }
    }

    @GetMapping("rest/orders")
    public List<Order> fillAllOrder(){
        return orderService.fillAllOrder();
    }


    @GetMapping("rest/orderGhn")
    public Order orderGhn(@RequestParam("orderId") int orderId) throws JsonProcessingException {
        return orderService.orderGhn(orderId);
    }

    @GetMapping("rest/cancel-order")
    public Map<String, Object> cancelOrder(@RequestParam("orderId") int orderId) throws JsonProcessingException {
        Map<String, Object> model =  new HashMap<>();
        model.put("order", orderService.cancelOrder(orderId));
        return model;
    }

    public String vnPay(Order order){
        int idOrder = order.getId();
        int total = 0;
        if(order.getVoucher() == null){
             total = (int) ((order.getTotal() + order.getShipFee()) );

        }else{
             total = (int) ((order.getTotal() + order.getShipFee()) - order.getVoucher().getDiscountPrice());
        }

        System.out.println();
        String content = "Thanh toán đơn hàng ";
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        baseUrl += "/user/payment-order-user";
        System.out.println(baseUrl);
        String vnpayUrl = vnPayService.createOrder(total, content, baseUrl,idOrder);
        return  vnpayUrl;
    }

    @GetMapping("rest/orders/update-status")
    public ResponseEntity<?> updateOrder(@RequestParam("idOrder") int idOrder, @RequestParam("statusOrder") int statusOrder) {
        return ResponseEntity.ok(orderService.updateStatusOrder(idOrder,statusOrder));
    }


}
