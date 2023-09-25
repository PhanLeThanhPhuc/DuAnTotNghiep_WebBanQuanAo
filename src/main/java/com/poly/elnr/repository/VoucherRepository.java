package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>{

}
