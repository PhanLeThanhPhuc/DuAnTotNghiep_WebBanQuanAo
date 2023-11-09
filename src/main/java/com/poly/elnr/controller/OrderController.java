package com.poly.elnr.controller;

import com.poly.elnr.entity.Order;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    VnPayService vnPayService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    OrderService orderService;

    @GetMapping("user/information-order")
    public String informationOrder(@RequestParam("idorder") int idOrder, Model model){
        model.addAttribute("order", orderService.fillOrderById(idOrder));
        return "user/product/information-order";
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(Model model){
        //0 fail, 1 success
        int paymentStatus =vnPayService.orderReturn(request);
        int idorder = Integer.parseInt(request.getParameter("vnp_TxnRef"));

        Order order = new Order();

        if(paymentStatus==1){
            order= orderService.updateStatusPayment(idorder, paymentStatus);
        }else{
            order = orderService.updateStatusPayment(idorder, paymentStatus);
        }
        model.addAttribute("order", order);
        return "user/product/information-order";
    }
}
