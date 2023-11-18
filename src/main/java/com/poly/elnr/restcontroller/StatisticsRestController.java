package com.poly.elnr.restcontroller;

import com.poly.elnr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsRestController {

    @Autowired
    OrderService orderService;

    @GetMapping("/rest/orderTotal")
    ResponseEntity<?> getAllTotalOrder (){
        return ResponseEntity.ok(orderService.findAllTotal());
    }

    @GetMapping("/rest/orderPhoneAndDate")
    ResponseEntity<?> findTop10ByPhonePriceWithDate(){
        return ResponseEntity.ok(orderService.findTop10ByPhonePriceWithDate());
    }

    @GetMapping("/rest/order-total-user")
    ResponseEntity<?> findTotalByPhoneAndDateRange(){
        return ResponseEntity.ok(orderService.findTotalByPhoneAndDateRange());
    }
}
