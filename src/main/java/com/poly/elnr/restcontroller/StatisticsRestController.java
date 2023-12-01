package com.poly.elnr.restcontroller;

import com.poly.elnr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
public class StatisticsRestController {

    @Autowired
    OrderService orderService;

    @GetMapping("/rest/orderTotal")
    ResponseEntity<?> getAllTotalOrder (){
        return ResponseEntity.ok(orderService.findAllTotal());
    }

    @GetMapping("/rest/phone-total")
    ResponseEntity<?> findAllByPhonePriceWithDate(){
        return ResponseEntity.ok(orderService.findPhoneTotalDTO());
    }

    @GetMapping("/rest/order-total-user")
    ResponseEntity<?> findTotalByPhoneAndDateRange(){
        return ResponseEntity.ok(orderService.findTotalByPhoneAndDateRange());
    }

    @GetMapping("/rest/top-totals-by-date-range")
    ResponseEntity<?> findTopTotalsByDateRange(@RequestParam("start-date")String startDate, @RequestParam("end-date")String endDate) throws ParseException {
        System.out.println();
        return ResponseEntity.ok(orderService.findTop10PhoneTotalsByDateRange(startDate, endDate));
    }

    @GetMapping("/rest/phone-total-for-today")
    ResponseEntity<?> findPhoneTotalsForToday() throws ParseException {
        System.out.println();
        return ResponseEntity.ok(orderService.findPhoneTotalsForToday());
    }
}
