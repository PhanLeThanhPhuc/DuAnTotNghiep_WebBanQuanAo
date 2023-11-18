package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer>{

}
