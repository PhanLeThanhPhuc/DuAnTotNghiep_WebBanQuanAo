package com.poly.elnr.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiGetAddress {

    @Value("${token.api}")
    private String token;

    String urlProvince = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    String urlDistrict = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=";
    String urlWard = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=";

    RestTemplate rest = new RestTemplate();

    HttpHeaders httpHeaders = new HttpHeaders();

    public String listProvinces() {
        httpHeaders.set("token", token);
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(urlProvince, HttpMethod.GET, entity, String.class);

        return responseEntity.getBody();
    }

    public String listDistrict(int province_id) {
        String url = urlDistrict + province_id;
        httpHeaders.set("token", token);
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, entity, String.class);

        return responseEntity.getBody();
    }

    public String listWard(int district_id) {
        String url = urlWard + district_id;
        httpHeaders.set("token", token);
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, entity, String.class);

        return responseEntity.getBody();
    }
}
