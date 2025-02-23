package com.poly.elnr.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="voucher")
@Entity
public class Voucher implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "voucher")
    private String voucher;

    @Column(name = "discount_price")
    private double discountPrice;
    
    @Column(name = "min")
    private double min;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private boolean status;
    
   
    
    @Column(name = "active")
    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher")
    private List<Order> order;

   //add new column
    
    @Column(name = "max_price")
    private double maxPrice;
    
    @Column(name = "product_id")
    private String productID;
    @Column(name = "min_product")
    private int minOrderProduct;
    
    @Column(name = "ismax")
    private boolean is_max;
    @Column(name = "description")
    private String description;


}