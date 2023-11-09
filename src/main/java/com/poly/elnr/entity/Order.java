package com.poly.elnr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.elnr.dto.OrderData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="orders")
@Entity
public class Order implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "name_user")
    private String nameUser;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private int status;

    @Column(name = "payment")
    private int payment;

    @Column(name = "status_payment")
    private int statusPayment;

    @Column(name = "ship_code")
    private String shipCode;

    @Column(name = "ship_fee")
    private int shipFee;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "total")
    private int total;
    
    @Column(name = "total_discount")
    private int totalDiscount;
    
    @Column(name = "weight")
    private int weight;

    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "district_id")
    private int districtId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

//    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

}
