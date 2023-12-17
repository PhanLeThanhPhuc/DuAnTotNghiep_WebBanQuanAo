package com.poly.elnr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.poly.elnr.dto.OrderDTO;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.dto.PhoneTotalDTO;
import com.poly.elnr.dto.TotalWithUserOrderDTO;
import com.poly.elnr.entity.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> fillAllOrder();
    void addSseEmitter(SseEmitter emitter);

    void removeSseEmitter(SseEmitter emitter) ;

    Order fillOrderById(int id);

    double subTotalOrder(int idOrder);

    Order saveOrder(JsonNode orderData) throws JsonProcessingException;

    Order cancelOrder(int orderId) throws IOException;

    Order updateStatusPayment(int idOrder, int statusPayment, String paymentTime);

    Order orderGhn(int orderId) throws JsonProcessingException;

    List<Order> findOrderByIdUser(String username);

    Order updatePayment(int idOrder, int statusPayment);

    Order updatePaymentAndStatusPayment(int idOrder, int statusPayment, int payment, String paymentTime);

    List<OrderDTO> findAllTotal();

    List<PhoneTotalDTO> findPhoneTotalDTO();

    List<TotalWithUserOrderDTO> findTotalByPhoneAndDateRange();

    List<PhoneTotalDTO> findTop10PhoneTotalsByDateRange(String startDate, String endDate) throws ParseException;

    List<PhoneTotalDTO> findPhoneTotalsForToday();

    Order updateStatusOrder(int id, int status);

    Order findOrderByPhoneAndId(String phone, int idOrder);

}