package com.poly.elnr.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.elnr.dto.OrderData;
import com.poly.elnr.service.ApiGHNService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiGHNServiceImpl implements ApiGHNService {

    @Value("${token.api}")
    private String token;

    String urlReturnOrder = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    String urlCreateOrder = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

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

//    @Override
//    public Map<String, Object> CreateOrderGHN(OrderData orderData) throws JsonProcessingException {
//
//        String jsonData = objectMapper.writeValueAsString(orderData.getOrderDataGHN());
//
//        httpHeaders.set("token", token);
//        httpHeaders.set("ShopId", "190216");
//        httpHeaders.set("Content-Type", "application/json");
//
//        HttpEntity<String> entity = new HttpEntity<>(jsonData, httpHeaders);
//
//        ResponseEntity<Map<String, Object>> responseEntity = rest.exchange(urlCreateOrder, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
//        responseEntity.getBody();
//        System.out.println();
//        return responseEntity.getBody();
//    }


}
