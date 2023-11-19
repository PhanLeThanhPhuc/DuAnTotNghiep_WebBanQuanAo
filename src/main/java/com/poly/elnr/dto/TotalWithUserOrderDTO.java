package com.poly.elnr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TotalWithUserOrderDTO {

    String phone;
    double total;
    Date date;

}
