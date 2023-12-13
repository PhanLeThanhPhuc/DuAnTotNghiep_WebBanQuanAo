package com.poly.elnr.controller;

import com.poly.elnr.entity.Order;
import com.poly.elnr.service.OrderService;
import com.poly.elnr.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println("THời gian thanh toán: "+ request.getParameter("vnp_PayDate"));
        Order order = new Order();

        if(paymentStatus==1){
            order= orderService.updateStatusPayment(idorder, paymentStatus);
        }else{
            order = orderService.updateStatusPayment(idorder, paymentStatus);
        }
        model.addAttribute("order", order);
        return "user/product/information-order";
    }

    @GetMapping("/user/search-order")
    public String searchOrder(){
        return "user/product/search-order";
    }

    @GetMapping("/user/search-order-result")
    public String searchOrderResult(Model model, @RequestParam("idOrder") int idOrder){
        model.addAttribute("subTotal",orderService.subTotalOrder(idOrder));
        model.addAttribute("order", orderService.fillOrderById(idOrder));
        return "user/product/search-order-result";
    }

    @GetMapping("rest/order/search-order")
    public String searchOrder(@RequestParam("phone") String phone, @RequestParam("id-order") int idOrder, Model model) {
        Order order = orderService.findOrderByPhoneAndId(phone, idOrder);
        if(order == null){
            model.addAttribute("message","Không tìm thấy đơn hàng");
            return "user/product/search-order";
        }else{
            return "redirect:/user/search-order-result?idOrder="+order.getId();
        }
    }
}
