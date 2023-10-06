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
@Table(name="Category")
@Entity
public class Category implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_insert")
    private Date dateInsert;

    @Column(name = "date_update")
    private Date dateUpdate;

    @Column(name = "status")
    private boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
	List<CategoryDetail> category_detail;

}