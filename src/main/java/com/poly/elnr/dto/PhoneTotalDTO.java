package com.poly.elnr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PhoneTotalDTO {

    String phone;
    double total;
    Long quantityOrder;
}
