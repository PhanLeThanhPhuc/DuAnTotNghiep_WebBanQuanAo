package com.poly.elnr.restcontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.poly.elnr.dto.OrderData;
//import com.poly.elnr.service.ApiGHNService;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderRestController {

    @Autowired
    VnPayService vnPayService;

//    @Autowired
//    ApiGHNService apiGHNService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    OrderService orderService;

//    @PostMapping("/user/order")
//    public Map<String, Object> order(@RequestBody OrderData orderData, HttpServletResponse response) throws IOException {
//        int payment = orderData.getOrder().getPayment();
//        if(payment ==1){
//			orderService.saveOrder(orderData);
//            Map<String, Object> model = new HashMap<>();
//            model.put("payment", 1);
//            model.put("returnCreateOrderGHN", apiGHNService.CreateOrderGHN(orderData));
//            return model;
//        }else{
//            orderService.saveOrder(orderData);
//            Map<String, Object> model = new HashMap<>();
//            model.put("payment", 2);
//            model.put("vnPayUrl", vnPay(orderData));
//            model.put("returnCreateOrderGHN",  apiGHNService.CreateOrderGHN(orderData));
//            return model;
//        }
//    }

    @PostMapping("/user/order")
    public Map<String, Object> order (@RequestBody JsonNode orderData, HttpServletResponse response) throws IOException {
        System.out.println();
        int payment = Integer.parseInt(orderData.get("payment").asText());
        if(payment ==1){
			orderService.saveOrder(orderData);
            Map<String, Object> model = new HashMap<>();
            model.put("payment", 1);
//            model.put("returnCreateOrderGHN", apiGHNService.CreateOrderGHN(orderData));
            return model;
        }else{
            orderService.saveOrder(orderData);
            Map<String, Object> model = new HashMap<>();
            model.put("payment", 2);
//            model.put("vnPayUrl", vnPay(orderData));
//            model.put("returnCreateOrderGHN",  apiGHNService.CreateOrderGHN(orderData));
            return model;
        }
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(Model model){
        //0 fail, 1 success
        int paymentStatus =vnPayService.orderReturn(request);
        System.out.println("status: "+paymentStatus);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

//		model.addAttribute("orderId", orderInfo);
//		model.addAttribute("totalPrice", totalPrice);
//		model.addAttribute("paymentTime", paymentTime);
//		model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }

//    public String vnPay(OrderData orderData){
////        int total = orderData.getOrder().getTotal();
//        String content = "Thanh toán đơn hàng ";
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        System.out.println(baseUrl);
//        String vnpayUrl = vnPayService.createOrder(total, content, baseUrl);
//        return vnpayUrl;
//    }
}
