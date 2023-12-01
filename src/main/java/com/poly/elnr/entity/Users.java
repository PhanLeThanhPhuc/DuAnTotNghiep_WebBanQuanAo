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
import org.hibernate.annotations.BatchSize;

@AllArgsConstructor
@NoArgsConstructor
//@Data
@Getter
@Setter
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

    @Column(name = "signup")
    private boolean signup;

    @Column(name = "is_password_reset")
    private boolean isPasswordReset;

    @Column(name = "otp")
    private int otp;

    @Column(name = "time_otp")
    private Date timeOtp;

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
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    private List<Authority> authorities;

    public Users(int id) {
        this.id = id;
    }

}

