package com.poly.elnr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="discount")
@Entity
public class Discount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column(name = "name")
    private String name;

    @Column(name = "discount")
    private int discount;
    
    @Column(name = "startdate")
    private Date startdate;
    
    @Column(name = "enddate")
    private Date enddate;
    
    @Column(name = "active")
    private boolean active;
    
    @Column(name = "product_id")
    private String product_id;


    @Column(name = "allproduct")
    private boolean allproduct;
    
    
    
    
    
}
