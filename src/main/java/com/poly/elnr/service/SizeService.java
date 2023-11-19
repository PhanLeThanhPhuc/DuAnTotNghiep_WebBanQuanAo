package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Size;

public interface SizeService {

	List<Size> findAllSizeStatusTrue();

	List<Size> findAll();

	Size findById(Integer id);

	void create(Size size);

	Size update(Size size);

	void delete(Integer id);
	
}
