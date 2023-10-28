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
@Table(name = "Product")
@Entity
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "discount_price")
	private double discountPrice;

	@Column(name = "price")
	private double price;

	@Column(name = "is_sale")
	private boolean isSale;

	@Column(name = "status")
	private boolean status;

	@Column(name = "date_insert")
	private Date dateInsert;

	@Column(name = "date_update")
	private Date dateUpdate;

	@Column(name = "thumbnail")
	private String thumbnail;

	@ManyToOne
	@JoinColumn(name = "categoryDetail_id")
	private CategoryDetail categoryDdetail;

	@ManyToOne
	@JoinColumn(name = "color_id")
	private Color color;

	@ManyToOne
	@JoinColumn(name = "description_id")
	private Description description;


	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private List<ImageProduct> images;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private List<ProductDetails> productDetails;

	@JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

	@JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<DiscountDetail> discountDetail;
   
    
    

}

