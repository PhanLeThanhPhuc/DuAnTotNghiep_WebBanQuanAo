package com.poly.elnr.dto;

import com.poly.elnr.entity.OrderDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
public class OrderData {
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_province_name;
    private int cod_amount;
    private String content;
    private String to_ward_code;
    private int to_district_id;
    private int weight;
    private int service_type_id = 2;
    private int payment_type_id = 1;
    private String note;
    private String required_note = "CHOXEMHANGKHONGTHU";
    private List<Item> items;

}

