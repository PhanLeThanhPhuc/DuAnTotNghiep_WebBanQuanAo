package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Voucher;


public interface VoucherService {

	List<Voucher> findAll();

	Voucher findById(Integer id);

	void create(Voucher voucher);

	Voucher update(Voucher voucher);

	void delete(Integer id);

}
