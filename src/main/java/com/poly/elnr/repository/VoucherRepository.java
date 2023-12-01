package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Voucher;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>{

    @Query("SELECT v FROM Voucher v WHERE CURRENT_TIMESTAMP BETWEEN v.startDate AND v.endDate AND v.status = true")
    List<Voucher> fillAllVoucherDate();
}
