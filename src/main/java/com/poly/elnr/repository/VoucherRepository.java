package com.poly.elnr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.elnr.entity.Voucher;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>{

    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM voucher\n" +
            "WHERE CURRENT_TIMESTAMP BETWEEN start_date AND end_date;", nativeQuery = true)
    List<Voucher> fillAllVoucherDate();
}
