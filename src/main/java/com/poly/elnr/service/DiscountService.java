package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Discount;

public interface DiscountService {

	List<Discount> findAll();

	void create(Discount discount);

	Discount update(Discount discount);

	void delete(Integer id);

	Discount findById(Integer id);

}
