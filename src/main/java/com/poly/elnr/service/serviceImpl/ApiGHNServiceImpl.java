package com.poly.elnr.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.elnr.dto.Item;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.entity.Order;
import com.poly.elnr.service.ApiGHNService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiGHNServiceImpl implements ApiGHNService {

    @Value("${token.api}")
    private String token;

    String urlReturnOrder = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    String urlCreateOrder = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

    String urlCancelOrder = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel?order_codes=";

    RestTemplate rest = new RestTemplate();

    HttpHeaders httpHeaders = new HttpHeaders();

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> shipFee(Map<String, Object> objectProduct) throws JsonProcessingException {
        String jsonData = objectMapper.writeValueAsString(objectProduct);

        httpHeaders.set("token", token);
        httpHeaders.set("ShopId", "190216");
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(jsonData, httpHeaders);

        ResponseEntity<Map<String, Object>> responseEntity = rest.exchange(urlReturnOrder, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});

        return responseEntity.getBody();
    }

    @Override
    public Map<String, Object> CreateOrderGHN(Order order) throws JsonProcessingException {

        OrderData orderData = createOrderGhn(order);

        String jsonData = objectMapper.writeValueAsString(orderData);

        httpHeaders.set("token", token);
        httpHeaders.set("ShopId", "190216");
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(jsonData, httpHeaders);

        ResponseEntity<Map<String, Object>> responseEntity = rest.exchange(urlCreateOrder, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});

        System.out.println();
        return responseEntity.getBody();
    }

    @Override
    public Map<String, Object> cancelOrder(String idOrderGhn) throws JsonProcessingException {
        String url = urlCancelOrder + idOrderGhn;
        httpHeaders.set("token", token);
        httpHeaders.set("ShopId", "190216");
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Map<String, Object>> responseEntity = rest.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return responseEntity.getBody();
    }

    public OrderData createOrderGhn (Order order){
        OrderData orderData = new OrderData();

        orderData.setTo_name(order.getNameUser());
        orderData.setTo_phone(order.getPhone());
        orderData.setTo_address(order.getDetailAddress()+ ", "+ order.getWard()+ ", " + order.getDistrict() + ", " + order.getProvince());
        orderData.setTo_province_name(order.getProvince());
        orderData.setCod_amount(order.getTotal()-order.getTotalDiscount()+order.getShipFee());
        //1 ck 0 tien mat
        if(order.getStatusPayment() == 1){
            orderData.setCod_amount(0);
        }else{
            orderData.setCod_amount(order.getTotal()-order.getTotalDiscount()+order.getShipFee());
        }
        orderData.setTo_ward_code(order.getWardCode());
        orderData.setTo_district_id(order.getDistrictId());
        orderData.setWeight(order.getWeight());
        orderData.setNote(order.getNote());
        List<Item> items = new ArrayList<>();
        order.getOrderDetails().forEach(list->{
            Item itemEntity = new Item();
            itemEntity.setName(list.getProductDetails().getProduct().getName());
            itemEntity.setQuantity(list.getQuantity());
            items.add(itemEntity);
        });
        orderData.setItems(items);
        return orderData;
    }
}
