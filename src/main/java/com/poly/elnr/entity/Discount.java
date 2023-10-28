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
    @Temporal(TemporalType.DATE)
    private Date startdate;
    
    @Column(name = "enddate")
    @Temporal(TemporalType.DATE)
    private Date enddate;
    
    @Column(name = "active")
    private boolean active;
    
    @JsonIgnore
    @OneToMany(mappedBy = "discount")
    private List<DiscountDetail> discountDetail;
    
    
    
}
