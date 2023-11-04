package com.poly.elnr.dto;

import com.poly.elnr.entity.OrderDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
public class OrderData {
    private String province;
    private String district;
    private String ward;
    private String detailAddress;
    private String nameUser;
    private Date orderDate;
    private String phone;
    private int payment;
    private String shipCode;
    private int shipFee;
    private String email;
    private int total;
    private int totalDiscount;
    private int weight;
    private String wardCode;
    private int districtId;
    private List<OrderDetail> orderDetails;

}


