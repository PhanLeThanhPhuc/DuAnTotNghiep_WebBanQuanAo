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
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="users")
@Entity
public class Users implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private int gender;

    @Column(name = "status")
    private boolean status;

    @Column(name = "date_insert")
    private Date date_insert;
    
    @Column(name = "date_update")
    private Date date_update;
    
    @Column(name = "image")
    private String image;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Authority> authorities;

}

