package com.poly.elnr.entity;

import java.util.Date;
import java.util.List;

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
@Table(name="Description")
@Entity
public class Description {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column(name = "quantity")
    private int quantity;

    @Column(name = "weight")
    private String weight;

    @Column(name = "material")
    private String material;

    @Column(name = "technology")
    private String technology;
    
    @Column(name = "manufacture")
    private String manufacture;
    
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "description")
	private List<Product> product;
    
}