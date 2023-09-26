package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

}